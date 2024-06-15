package com.capstonehore.ngelana.view.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.ProfileAdapter
import com.capstonehore.ngelana.data.local.entity.Profile
import com.capstonehore.ngelana.data.preferences.ThemeManager
import com.capstonehore.ngelana.databinding.FragmentProfileBinding
import com.capstonehore.ngelana.view.home.HomeViewModel
import com.capstonehore.ngelana.view.login.LoginActivity
import com.capstonehore.ngelana.view.main.ThemeViewModel
import com.capstonehore.ngelana.view.main.ThemeViewModelFactory
import com.capstonehore.ngelana.view.profile.aboutus.AboutUsActivity
import com.capstonehore.ngelana.view.profile.favorite.MyFavoriteActivity
import com.capstonehore.ngelana.view.profile.helpcenter.HelpCenterActivity
import com.capstonehore.ngelana.view.profile.interest.MyInterestActivity
import com.capstonehore.ngelana.view.profile.language.LanguageActivity
import com.capstonehore.ngelana.view.profile.personalinformation.PersonalInformationActivity
import com.capstonehore.ngelana.view.profile.review.MyReviewActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private val accountList = ArrayList<Profile>()
    private val settingsList = ArrayList<Profile>()
    private val informationList = ArrayList<Profile>()

    private lateinit var themeManager: ThemeManager
    private lateinit var themeViewModel: ThemeViewModel

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(THEME_SETTINGS)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
        setupData()
        themeSettings()
    }

    private fun setupAction() {
        binding.signOutButton.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                val message = getString(R.string.sign_out_confirmation_message)

                setTitle(getString(R.string.sign_out))
                setMessage(message)

                setPositiveButton(getString(R.string.sign_out)) { _, _ ->
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    requireActivity().finish()
                }

                setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }

                create().show()
            }
        }
    }

    private fun setupData() {
        setupRecyclerView(binding.rvAccount)
        setupRecyclerView(binding.rvSettings)
        setupRecyclerView(binding.rvInformation)

        accountList.addAll(
            getListProfile(
                R.array.data_account_code,
                R.array.data_account_name,
                R.array.data_account_icon
            )
        )
        settingsList.addAll(
            getListProfile(
                R.array.data_settings_code,
                R.array.data_settings_name,
                R.array.data_settings_icon
            )
        )
        informationList.addAll(
            getListProfile(
                R.array.data_information_code,
                R.array.data_information_name,
                R.array.data_information_icon
            )
        )

        showList(binding.rvAccount, accountList) { item ->
            val intent = when (item.code) {
                "ngelana-personal-information" -> Intent(
                    requireContext(),
                    PersonalInformationActivity::class.java
                )
                "ngelana-my-interest" -> Intent(requireContext(), MyInterestActivity::class.java)
                "ngelana-my-favorite" -> Intent(requireContext(), MyFavoriteActivity::class.java)
                "ngelana-my-review" -> Intent(requireContext(), MyReviewActivity::class.java)
                else -> null
            }
            intent?.let { startActivity(it) }
        }

        showList(binding.rvSettings, settingsList) { item ->
            val intent = when (item.code) {
                "ngelana-language" -> Intent(requireContext(), LanguageActivity::class.java)
                else -> null
            }
            intent?.let { startActivity(it) }
        }

        showList(binding.rvInformation, informationList) { item ->
            val intent = when (item.code) {
                "ngelana-help-center" -> Intent(requireContext(), HelpCenterActivity::class.java)
                "ngelana-about-us" -> Intent(requireContext(), AboutUsActivity::class.java)
                else -> null
            }
            intent?.let { startActivity(it) }
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun getListProfile(codeResId: Int, namesResId: Int, iconsResId: Int): ArrayList<Profile> {
        val dataCode = resources.getStringArray(codeResId)
        val dataName = resources.getStringArray(namesResId)
        val dataIcon = resources.obtainTypedArray(iconsResId)
        val listProfile = ArrayList<Profile>()
        for (i in dataName.indices) {
            val profile = Profile(dataCode[i], dataName[i], dataIcon.getResourceId(i, -1))
            listProfile.add(profile)
        }
        dataIcon.recycle()
        return listProfile
    }

    private fun showList(
        recyclerView: RecyclerView,
        list: ArrayList<Profile>,
        onItemClick: (Profile) -> Unit
    ) {
        val profileAdapter = ProfileAdapter(list)
        recyclerView.adapter = profileAdapter

        profileAdapter.setOnItemClickCallback(object : ProfileAdapter.OnItemClickCallback {
            override fun onItemClicked(items: Profile) {
                onItemClick(items)
            }
        })
    }

    private fun themeSettings() {
        themeManager = ThemeManager.getInstance(requireContext().dataStore)

        themeViewModel = ViewModelProvider(
            requireActivity(),
            ThemeViewModelFactory(themeManager)
        )[ThemeViewModel::class.java]

        themeViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchThemes.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchThemes.isChecked = false
            }
        }

        binding.switchThemes.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            themeViewModel.saveThemeSetting(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val THEME_SETTINGS = "theme_settings"
    }

}