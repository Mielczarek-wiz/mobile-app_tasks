package com.example.sensors

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task


class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var client : FusedLocationProviderClient
    private lateinit var locationManager: LocationManager

    private lateinit var backLi: LinearLayout

    private lateinit var ambientTemperature: TextView
    private lateinit var humidityView: TextView
    private lateinit var lightView: TextView
    private lateinit var accelerateView: TextView
    private lateinit var magnetometerView: TextView

    private lateinit var temperatureThread: Thread
    private lateinit var locationThread: Thread

    private var cancellationToken = CancellationTokenSource()

    private var temperature: Sensor? = null
    private var humidity: Sensor? = null
    private var lightS: Sensor? = null
    private var accelerateS: Sensor? = null
    private var magnetometerS: Sensor? = null


    private var isTemp: Boolean = false
    private var isHumidity: Boolean = false
    private var isLightS: Boolean = false
    private var isAccelerateS: Boolean = false
    private var isMagnetometerS: Boolean = false

    private var valueOfTemperature : Double = 0.0


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeAll()

        temperatureThread.start()

        locationThread.start()
    }
    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent) {
        takeCareOfSensors(event)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onResume() {
        super.onResume()
        if (isTemp) {
            sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL)
        }
        if(isHumidity){
            sensorManager.registerListener(this, humidity, SensorManager.SENSOR_DELAY_NORMAL)
        }
        if(isLightS){
            sensorManager.registerListener(this, lightS, SensorManager.SENSOR_DELAY_NORMAL)
        }
        if(isAccelerateS){
            sensorManager.registerListener(this, accelerateS, SensorManager.SENSOR_DELAY_NORMAL)
        }
        if(isMagnetometerS){
            sensorManager.registerListener(this, magnetometerS, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
    override fun onStop() {
        super.onStop()

        cancellationToken.cancel()
    }

    @SuppressLint("SetTextI18n")
    private fun checkSensors(){
        if(temperature != null) {
            isTemp = true
            ambientTemperature.text = "\nTemperature sensor is available!\n"
        }
        else{
            ambientTemperature.text = "\nTemperature sensor is not available!\n"
        }
        if(humidity != null){
            isHumidity = true
        }
        else{
            humidityView.text = "\nHumidity sensor is not available!\n"
        }

        if(lightS != null){
            isLightS = true
        }
        else{
            lightView.text = "\nLight sensor is not available!\n"
        }

        if(accelerateS != null){
            isAccelerateS = true
        }
        else{
            accelerateView.text = "\nAccelerate sensor is not available!\n"
        }
        if(magnetometerS != null){
            isMagnetometerS = true
        }
        else{
            magnetometerView.text = "\nMagnet field sensor is not available!\n"
        }

    }
    @SuppressLint("SetTextI18n")
    private fun takeCareOfSensors(event : SensorEvent){
        if(event.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE){
            valueOfTemperature = event.values[0].toDouble()
        }
        if(event.sensor.type == Sensor.TYPE_RELATIVE_HUMIDITY){
            humidityView.text = "Humidity: ${event.values[0]} %"
        }
        if(event.sensor.type == Sensor.TYPE_LIGHT){
            lightView.text = "Light: ${event.values[0]} lux"
            changeBack(event.values[0].toLong())
        }
        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){
            onAccelerateChange(event.values[0].toDouble(), event.values[1].toDouble(), event.values[2].toDouble())
        }
        if(event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD){
            magnetometerView.text="Magnetometer: x: ${event.values[0]}  y: ${event.values[1]} z: ${event.values[2]}"
        }
    }
    @SuppressLint("SetTextI18n")
    private fun takeCoordinates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
            PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PERMISSION_GRANTED) {

            val currentLocationTask: Task<Location> = client.getCurrentLocation(PRIORITY_HIGH_ACCURACY, cancellationToken.token)

            currentLocationTask.addOnCompleteListener { task: Task<Location> ->
                val result = if (task.isSuccessful) {
                    "\nCoordinates: ${task.result.latitude}, ${task.result.longitude}\n"
                } else {
                    "\nError: ${task.exception}\n"
                }
                findViewById<TextView>(R.id.coordinatesView).text = result
            }
        }
        else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 44)
        }
    }
    private fun initializeAll(){
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        client=getFusedLocationProviderClient(this)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        backLi = findViewById(R.id.layout)

        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        humidity  = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        lightS  = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        accelerateS  = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometerS = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        ambientTemperature = findViewById(R.id.ambientTemperatureView)
        humidityView = findViewById(R.id.humidityView)
        lightView = findViewById(R.id.lightView)
        accelerateView = findViewById(R.id.accelerateView)
        magnetometerView = findViewById(R.id.magnetometerView)

        temperatureThread = Thread {
            run{
                while (true) {
                    Thread.sleep((1000*60).toLong())
                    runOnUiThread { Toast.makeText(applicationContext,"Temperature: $valueOfTemperature °C", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        locationThread = Thread {
            run{
                while (true) {
                    takeCoordinates()
                    Thread.sleep((1000*5).toLong())
                }
            }
        }
        checkSensors()
    }
    private fun changeBack(light: Long){
        if(light > 12500){
            backLi.setBackgroundColor(Color.parseColor("#fdfdfd"))
        }
        else{
            backLi.setBackgroundColor(Color.parseColor("#403F3F"))
        }
    }
    @SuppressLint("SetTextI18n")
    private fun onAccelerateChange(x: Double, y: Double, z: Double){
        if(x == 0.0 && y > 9.8 && z == 0.0){
            accelerateView.text = "\nAccelerate: \nx:  $x m/s2 \ny:  $y m/s2, \nz:  $z m/s2 \nThis is fine..\n"
        }
        else{
            accelerateView.text = "\nAccelerate: \nx:  $x m/s2 \ny:  $y m/s2, \nz:  $z m/s2 \nWhat are you doing?\n"
        }
    }
}