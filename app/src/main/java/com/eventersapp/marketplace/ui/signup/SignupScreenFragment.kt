package com.eventersapp.marketplace.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.SignupScreenFragmentBinding
import com.eventersapp.marketplace.ui.viewmodel.SignupViewModel
import com.eventersapp.marketplace.ui.viewmodelfactory.SignupViewModelFactory
import com.eventersapp.marketplace.util.*
import com.eventersapp.marketplace.util.AppConstants.FACEBOOK
import com.eventersapp.marketplace.util.AppConstants.GOOGLE
import com.eventersapp.marketplace.util.AppConstants.MOBILE_NUMBER_VERIFICATION
import com.eventersapp.marketplace.util.AppUtils.hideProgressBar
import com.eventersapp.marketplace.util.AppUtils.showProgressBar
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class SignupScreenFragment : Fragment(), KodeinAware, View.OnClickListener {

    companion object {
        const val GOOGLE_SIGN_IN = 101
    }

    override val kodein by closestKodein()
    private lateinit var dataBind: SignupScreenFragmentBinding
    private val factory: SignupViewModelFactory by instance()
    private val viewModel: SignupViewModel by lazy {
        ViewModelProvider(this, factory).get(SignupViewModel::class.java)
    }

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeGoogleSigin()
        setDeviceInfo()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBind = DataBindingUtil.inflate(
            inflater,
            R.layout.signup_screen_fragment,
            container,
            false
        )
        return dataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupAPICall()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_facebook_sign_in -> {
                dataBind.fbLoginButton.performClick()
            }
            R.id.button_google_sign_in -> {
                googleSignIn()
            }
            R.id.button_phone_number -> {
                findNavController().navigate(
                    R.id.action_signupScreenFragment_to_phoneNumberFragment
                )
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.i("Info", "firebaseAuthWithGoogle:" + account?.id)
                viewModel.firebaseAuthWithGoogle(account?.idToken ?: "")
            } catch (e: ApiException) {
                Log.i("Info", "Google sign in failed", e)
            }
        } else {
            // Pass the activity result back to the Facebook SDK
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }


    private fun setupUI() {
        dataBind.buttonFacebookSignIn.setOnClickListener(this)
        dataBind.buttonGoogleSignIn.setOnClickListener(this)
        dataBind.buttonPhoneNumber.setOnClickListener(this)
        facebookSignIn()
    }

    private fun setupAPICall() {
        viewModel.googleSignLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    showProgressBar(requireContext())
                }
                is State.Success -> {
                    requireActivity().showToast("Google sign in successful")
                    SharedPref.setStringPref(
                        requireContext(),
                        SharedPref.KEY_PROVIDER,
                        GOOGLE
                    )
                    connect()
                }
                is State.Error -> {
                    hideProgressBar()
                    dataBind.rootLayout.snackbar(state.message)
                }
            }
        })

        viewModel.facebookSignLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    showProgressBar(requireContext())
                }
                is State.Success -> {
                    requireActivity().showToast("Facebook sign in successful")
                    SharedPref.setStringPref(
                        requireContext(),
                        SharedPref.KEY_PROVIDER,
                        FACEBOOK
                    )
                    connect()
                }
                is State.Error -> {
                    hideProgressBar()
                    dataBind.rootLayout.snackbar(state.message)
                }
            }
        })

        viewModel.connectLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                }
                is State.Success -> {
                    hideProgressBar()
                    if (state.data.data.auth != null && state.data.data.auth.status == MOBILE_NUMBER_VERIFICATION) {
                        SharedPref.setStringPref(
                            requireContext(),
                            SharedPref.KEY_USER_ID,
                            state.data.data.user.userId
                        )
                        findNavController().navigate(
                            R.id.action_signupScreenFragment_to_phoneNumberFragment
                        )
                    } else {
                        SharedPref.setObjectPref(
                            requireContext(),
                            SharedPref.KEY_USER_DATA,
                            state.data
                        )
                        findNavController().navigate(R.id.action_signupScreenFragment_to_dashboardFragment)
                    }
                }
                is State.Error -> {
                    hideProgressBar()
                    dataBind.rootLayout.snackbar(state.message)
                }
            }
        })
    }

    private fun initializeGoogleSigin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }


    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
    }

    private fun facebookSignIn() {
        LoginManager.getInstance().logOut()
        callbackManager = CallbackManager.Factory.create()

        dataBind.fbLoginButton.fragment = this
        dataBind.fbLoginButton.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.i("Info", "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.i("Info", "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.i("Info", "facebook:onError", error)
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.i("Info", "handleFacebookAccessToken:$token")
        viewModel.firebaseAuthWithFacebook(token)
    }

    private fun connect() {
        viewModel.getJWTToken()
    }


    private fun setDeviceInfo() {
        viewModel.setDeviceInfo(requireContext().deviceId(), requireContext().manufacturer())
    }

}
