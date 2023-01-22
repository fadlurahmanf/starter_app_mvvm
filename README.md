# Starter App Kotlin MVVM

Android Kotlin menggunakan Architecture MVVM

### CI CD

Menggunakan Github actions. Setiap melakukan push, akan mentrigger testing & build APK

### Encrypt Decrypt

Encrypt & Decrypt menggunakan algoritma RSA

How to encrypt

1. Generate key
2. Simpan Public key & Private key di gradle.properties atau local.properties
3. gunakan public key untuk encrypt plain text, gunakan private key untuk decrypt text

![alt text](https://github.com/fadlurahmanf/starter_app_mvvm/blob/master/proof/encrypt.JPG?raw=true)

### Offline First

Data yang terambil di internet akan tersimpan oleh dao. Ketika melakukan fetch API ulang & mobile dalam keadaan offline, mobile akan mengambil data yang sudah diambil sebelumnya

### Download

Aplikasi ini menggunakan Download Manager dalam Foreground Service. Untuk update progress dalam notification, menggunakan Handler dan akan looping setiap 3 detik, untuk mengupdate progress sesuai NOTIFICATION_ID dalam Foreground Servicenya

![alt text](https://github.com/fadlurahmanf/starter_app_mvvm/blob/master/proof/download.jpg?raw=true)

### Call Notification

#### On Screen Notification
[Example Video](https://firebasestorage.googleapis.com/v0/b/firsttesting-631b4.appspot.com/o/call_notif_on_screen.MOV?alt=media&token=a99a4b13-c872-48c4-9fd6-7376b4bd7992)

#### On LockScreen Notification
[Example On LockScreen Notification](https://firebasestorage.googleapis.com/v0/b/firsttesting-631b4.appspot.com/o/call_notif_on_lock.MOV?alt=media&token=209853bf-e443-4090-8bd1-9d8d7af5d818)


### Chucker / HTTP Inspector

Aplikasi ini menggunakan [chucker](https://github.com/ChuckerTeam/chucker) library untuk melihat http request/response berdasarkan POV QA/Tester

#### Chucker Notification
![alt text](https://github.com/fadlurahmanf/starter_app_mvvm/blob/master/proof/chucker_3.jpg?raw=true)

#### Chucker Activity

Shaking phone inside activity which extend BaseActivity to launch ChuckActivity

![alt text](https://github.com/fadlurahmanf/starter_app_mvvm/blob/master/proof/chucker_1.jpg?raw=true)

#### Chucker Detail
![alt text](https://github.com/fadlurahmanf/starter_app_mvvm/blob/master/proof/chucker_2.jpg?raw=true)
