# Starter App Kotlin MVVM

Android Kotlin menggunakan Architecture MVVM

### CI CD

Menggunakan Github actions. Setiap melakukan push, akan mentrigger testing & build APK

### Offline First

Data yang terambil di internet akan tersimpan oleh dao. Ketika melakukan fetch API ulang & mobile dalam keadaan offline, mobile akan mengambil data yang sudah diambil sebelumnya

### Download

Aplikasi ini menggunakan Download Manager dalam Foreground Service. Untuk update progress dalam notification, menggunakan Handler dan akan looping setiap 3 detik, untuk mengupdate progress sesuai NOTIFICATION_ID dalam Foreground Servicenya
