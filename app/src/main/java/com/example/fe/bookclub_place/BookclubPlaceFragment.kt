package com.example.fe.bookclub_place

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.requestLocationUpdates
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.example.fe.JohnRetrofitClient
import com.example.fe.MainActivity
import com.example.fe.R
import com.example.fe.bookclub_book.server.BookClubRetrofitInterface
import com.example.fe.bookclub_place.api.PlaceSearchAPI
import com.example.fe.bookclub_place.api.PlaceSearchResponse
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.databinding.FragmentBookclubPlaceBinding
import com.example.fe.mypage.MyPageFragment
import com.example.fe.search.SearchMainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookclubPlaceFragment : Fragment() {

    private lateinit var binding: FragmentBookclubPlaceBinding
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    var currentLat: Double = 0.0
    var currentLon: Double = 0.0
    private var isFirstLoad = true // 처음 한 번만 실행하기 위한 플래그 추가

    private val searchActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val keyword = result.data?.getStringExtra("KEYWORD") ?: "검색 결과"
            Log.d("BookclubPlaceFragment", "✅ 받은 키워드: $keyword")
            updateKeywordToTitle(keyword)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookclubPlaceBinding.inflate(inflater, container, false)

        // 위치 서비스 초기화
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {

                val newLat = location.latitude
                val newLon = location.longitude

                // 위치가 변경되지 않았으면 API 호출 안 함
                if (currentLat == newLat && currentLon == newLon) return

                currentLat = newLat
                currentLon = newLon

                Log.d("BookclubPlaceFragment", "📍 현재 위치 업데이트: 위도=$currentLat, 경도=$currentLon")

                // 현재 위치가 변경될 때마다 API 호출
                fetchPlacesByLocation()
            }
        }

        // 앱 실행 시 최근 위치 가져와서 리스트 즉시 업데이트
//        getLastKnownLocationAndUpdateList()

        // GPS 및 네트워크 위치 업데이트 요청
        requestLocationUpdates()

        initBookclubPlaceSearchClickListener()
        initSearchClickListener()
        initProfileIconClickListener()

        return binding.root
    }

    // 최근 위치 가져와서 즉시 리스트 업데이트
    private fun getLastKnownLocationAndUpdateList() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1001)
            return
        }

        val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        if (lastKnownLocation != null) {
            currentLat = lastKnownLocation.latitude
            currentLon = lastKnownLocation.longitude
            Log.d("BookclubPlaceFragment", "📍 최근 위치 사용: 위도=$currentLat, 경도=$currentLon")

            // 처음 실행 시 한 번만 currentPlace를 받아서 bookclubPlaceTitleTv에 설정
            if (isFirstLoad) {
                fetchPlacesByLocation()
                isFirstLoad = false
            }
        } else {
            Log.d("BookclubPlaceFragment", "⚠️ 최근 위치를 가져올 수 없음. 위치 업데이트를 기다림.")
        }

        // 현재 위치 기반으로 리스트 즉시 업데이트
        updateBookclubPlaceListFragment()
    }

    private fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1001)
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // API 30 이상 (Android 11 이상)
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000L, // 5초마다 업데이트
                100f,   // 10m 이동 시 업데이트
                requireContext().mainExecutor, // Executor 추가
                locationListener
            )

            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5000L,
                100f,
                requireContext().mainExecutor,
                locationListener
            )

        } else {
            // API 29 이하 (Android 10 이하)
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000L,
                100f,
                locationListener
            )

            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5000L,
                100f,
                locationListener
            )
        }
    }

    // API 호출 후 currentPlace를 받아서 bookclubPlaceTitleTv 업데이트
    private fun fetchPlacesByLocation() {
        val api = JohnRetrofitClient.getClient(requireContext()).create(PlaceSearchAPI::class.java)
        api.sortPlaces(lat = currentLat, lon = currentLon).enqueue(object : Callback<PlaceSearchResponse> {

            override fun onResponse(call: Call<PlaceSearchResponse>, response: Response<PlaceSearchResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()?.result
                    val currentPlace = result?.currentPlace ?: binding.bookclubPlaceTitleTv.text.toString()
                    val profileImgUrl = result?.profileImg ?: ""

                    Log.d("BookclubPlaceFragment", "📍 API 응답 - 현재 위치 (currentPlace): $currentPlace")

                    requireActivity().runOnUiThread {
                        if (currentPlace != "알 수 없음") {
                            binding.bookclubPlaceTitleTv.text = currentPlace
                        }

                        if (profileImgUrl.isNotEmpty()) {
                            Glide.with(requireContext())
                                .load(profileImgUrl)
                                .circleCrop()
                                .into(binding.bookclubPlacePersonIc)
                        }

                        updateBookclubPlaceListFragment(currentPlace) // 🔹 UI 변경은 runOnUiThread 내부에서 실행
                    }
                } else {
                    Log.e("BookclubPlaceFragment", "❌ 장소 리스트 요청 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PlaceSearchResponse>, t: Throwable) {
                Log.e("BookclubPlaceFragment", "❌ 네트워크 오류: ${t.message}")
            }
        })
    }

    // 장소 검색 페이지
    private fun initBookclubPlaceSearchClickListener() {
        val commonClickListener = View.OnClickListener {
            val intent = Intent(requireContext(), BookclubPlaceSearchActivity::class.java)
            searchActivityLauncher.launch(intent)
        }

        binding.bookclubPlaceTitleTv.setOnClickListener(commonClickListener)
        binding.bookclubPlaceArrowDownIc.setOnClickListener(commonClickListener)
    }

    // 메인 검색 페이지
    private fun initSearchClickListener() {
        binding.bookclubPlaceSearchIc.setOnClickListener {
            val intent = Intent(requireContext(), SearchMainActivity::class.java)
            startActivity(intent)
        }
    }

    // 마이페이지로 이동
    private fun initProfileIconClickListener() {
        binding.bookclubPlacePersonIc.setOnClickListener {
            val mypageFragment = MyPageFragment()
            parentFragmentManager.commit {
                replace(R.id.bookclub_place_frm, mypageFragment)
//                addToBackStack(null)
            }

            (activity as? MainActivity)?.binding?.bottomNavigation?.selectedItemId = R.id.bottom_nav_mypage
        }
    }

    private fun updateKeywordToTitle(keyword: String) {
        Log.d("BookclubPlaceFragment", "✅ 키워드 업데이트: $keyword")

        requireActivity().runOnUiThread {
            if (isAdded) { // Fragment가 살아있을 때만 실행
                Log.d("BookclubPlaceFragment", "✅ 타이틀 업데이트됨: $keyword")
                binding.bookclubPlaceTitleTv.text = keyword
                updateBookclubPlaceListFragment(keyword)
            } else {
                Log.e("BookclubPlaceFragment", "❌ Fragment가 제거되어 타이틀을 업데이트할 수 없음.")
            }
        }
    }

    // 장소 리스트 프래그먼트 갱신
    private fun updateBookclubPlaceListFragment(keyword: String? = null) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.bookclub_place_list_frm, BookclubPlaceListFragment().apply {
                arguments = Bundle().apply {
                    putDouble("LAT", currentLat)
                    putDouble("LON", currentLon)
                    if (keyword != null) putString("KEYWORD", keyword)
                }
            })
//            .addToBackStack(null)
            .commit()
    }

    fun updateListByFilter(filter: String) {
        Log.d("BookclubPlaceFragment", "📌 필터 변경 요청: $filter")

        val fragment = childFragmentManager.findFragmentById(R.id.bookclub_place_list_frm)
        if (fragment is BookclubPlaceListFragment) {
            Log.d("BookclubPlaceFragment", "✅ BookclubPlaceListFragment에 필터 전달 시도")
            fragment.updateListByFilter(filter, currentLat, currentLon) // ✅ lat, lon 전달
        } else {
            Log.e("BookclubPlaceFragment", "❌ BookclubPlaceListFragment 찾을 수 없음. 다시 로드 시도")

            // Fragment 트랜잭션을 다시 수행하여 리스트 프래그먼트를 업데이트
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.bookclub_place_list_frm, BookclubPlaceListFragment().apply {
                    arguments = Bundle().apply {
                        putDouble("LAT", currentLat)
                        putDouble("LON", currentLon)
                    }
                })
                .commitNow()

            val updatedFragment = childFragmentManager.findFragmentById(R.id.bookclub_place_list_frm)
            if (updatedFragment is BookclubPlaceListFragment) {
                updatedFragment.updateListByFilter(filter, currentLat, currentLon)
                Log.d("BookclubPlaceFragment", "✅ BookclubPlaceListFragment 다시 로드 후 필터 적용 완료")
            } else {
                Log.e("BookclubPlaceFragment", "❌ BookclubPlaceListFragment 다시 로드 실패")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        locationManager.removeUpdates(locationListener) // 메모리 누수 방지
    }
}