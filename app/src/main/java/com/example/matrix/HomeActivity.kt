package com.example.matrix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.matrix.PrefsConstant.PAY_KEY_PREF
import com.example.matrix.databinding.ActivityHomeBinding
import ir.cafebazaar.poolakey.Connection
import ir.cafebazaar.poolakey.Payment
import ir.cafebazaar.poolakey.config.PaymentConfiguration
import ir.cafebazaar.poolakey.config.SecurityCheck
import ir.cafebazaar.poolakey.request.PurchaseRequest

class HomeActivity : AppCompatActivity() {

    var paymentConfiguration: PaymentConfiguration? = null
    var paymentConnection: Connection? = null
    var payment: Payment? = null

    private lateinit var sessionManager: SessionManager

    lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //اینجا یه نمونه ازش ساختم
        sessionManager = SessionManager(this)


        val localSecurityCheck = SecurityCheck.Enable(
            rsaPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwCsUqmhSygqyy6YmYlA9y4tR6BEF6r1M4se9kfL8FtkrzK/aPr//gXUDWDvDDLUN6CwGyadf0oGxbRrBENrh8png6UlJ3r6IW/r+Jm3HzjcjKyOqRj96a5XjPiAZWPBisUvLFLBPVnLKKHu1RlZYUnDk4MoS4JLUgXD/i52nqV3YKHfB4xYaFworhig6uGbl6jQFkzWPp6zQ/zTJ4V37s/68cTG+kzF/smVim+sRzkCAwEAAQ=="
        )

        paymentConfiguration = PaymentConfiguration(
            localSecurityCheck = localSecurityCheck
        )

        binding.btnPay.setOnClickListener {
            Pay()
        }

        binding.videoCardView.setOnClickListener {

            when (sessionManager.getStatePay(PAY_KEY_PREF)) {
                true ->  Toast.makeText(this,"این قسمت به زودی به برنامه اضافه میشود",Toast.LENGTH_SHORT).show()
                false -> {
                    Toast.makeText(this,"لطفا ابتدا نسخه کامل برنامه را خریداری نمایید",Toast.LENGTH_LONG).show()
                    Pay()
                }
            }
        }

        binding.matrixCardView.setOnClickListener {
            val parameterMatrix = Intent(this,MatrixActivity::class.java)
            startActivity(parameterMatrix)
        }
        binding.rulesCardView.setOnClickListener {

            when (sessionManager.getStatePay(PAY_KEY_PREF)) {
                true ->  {
                    val parameterRules = Intent(this,RulesActivity::class.java)
                    startActivity(parameterRules)
                }
                false -> {
                    Toast.makeText(this,"لطفا ابتدا نسخه کامل برنامه را خریداری نمایید",Toast.LENGTH_LONG).show()
                    Pay()
                }
            }

        }
        binding.adviceCardView.setOnClickListener {

            when (sessionManager.getStatePay(PAY_KEY_PREF)) {
                true ->  {
                    val parameterAdvice = Intent(this,AdviceActivity::class.java)
                    startActivity(parameterAdvice)
                }
                false -> {
                    Toast.makeText(this,"لطفا ابتدا نسخه کامل برنامه را خریداری نمایید",Toast.LENGTH_LONG).show()
                    Pay()
                }
            }

        }
        binding.successCardView.setOnClickListener {
            when (sessionManager.getStatePay(PAY_KEY_PREF)) {
                true ->  {
                    val parameterSuccess = Intent(this,SuccessActivity::class.java)
                    startActivity(parameterSuccess)
                }
                false -> {
                    Toast.makeText(this,"لطفا ابتدا نسخه کامل برنامه را خریداری نمایید",Toast.LENGTH_LONG).show()
                    Pay()
                }
            }

        }
        binding.jobsCardView.setOnClickListener {

            when (sessionManager.getStatePay(PAY_KEY_PREF)) {
                true ->  {
                    val parameterJobs = Intent(this, JobsActivity::class.java)
                    startActivity(parameterJobs)
                }
                false -> {
                    Toast.makeText(this,"لطفا ابتدا نسخه کامل برنامه را خریداری نمایید",Toast.LENGTH_LONG).show()
                    Pay()
                }
            }
        }
    }

    private fun Pay(){
        payment = Payment(context = this, config = paymentConfiguration!!)

        paymentConnection = payment?.connect {
            connectionSucceed {

                val purchaseRequest = PurchaseRequest(
                    productId = "2000",
                    payload = "PAYLOAD"
                )

                payment?.purchaseProduct(
                    registry = activityResultRegistry,
                    request = purchaseRequest
                ) {
                    purchaseFlowBegan {

                    }
                    failedToBeginFlow { throwable ->

                        Toast.makeText(applicationContext,"فرآیند هدایت به صفحه خرید با مشکل مواجه شد لطفا دوباره امتحان کنید",Toast.LENGTH_SHORT).show()

                    }
                    purchaseSucceed { purchaseEntity ->
                        sessionManager.saveStatePy(PAY_KEY_PREF,true)
                    }
                    purchaseCanceled {

                    }
                    purchaseFailed { throwable ->
                    }
                }

            }
            connectionFailed { throwable ->
                Toast.makeText(applicationContext,"اتصال به بازار برقرار نشد",Toast.LENGTH_SHORT).show()
            }
            disconnected {

            }
        }
    }

    override fun onDestroy() {
        paymentConnection?.disconnect()
        super.onDestroy()
    }
 }