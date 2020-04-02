# BolaSepak

  

## Deskripsi Aplikasi

  

BolaSepak adalah aplikasi berbasis *platform android* yang memberikan informasi terkini mengenai sepakbola. Aplikasi ini memiliki beberapa fitur, yaitu :

1. Memberikan *schedule* pertandingan sepakbola yang akan datang dan juga sudah lewat. 

2. Memberikan Event/Match detail untuk pertandingan yang telah lewat.

3. Memberikan keterangan tambahan untuk event/match yang belum dilaksanakan dengan menunjukkan lokasi pertandingan sepakbola dan juga cuaca di lokasi pertandingan tersebut.

4. Melihat *profile* dari sebuah tim beserta pertandingan yang sudah dijalani dan akan dijalani.

5. Memberikan notifikasi ketika suatu tim yang telah di-*subscribe* mengikuti pertandingan baru, dan

6. Fitur *step counter* untuk menghitung langkah.

  

## How it works?

1. Supaya pengguna dapat melihat *schedule*, dilakukan **fetching API** pada API [The Sports DB](https://thesportsdb.com/) kemudian mengambil data sesuai dengan kebutuhan (informasi mengenai *Match/Pertandingan* (IdEvent, info team yang bertanding, serta info keberjalanan pertandingan), *home team* (id, nama, url image), dan *away team* (dengan pengambilan atribut data yang sama dengan home team)). 
Penembakan API dilakukan beberapa kali dengan sasarannya yaitu List all Teams in a League, Next 15 Events by League Id, Next 5 Events by Team Id, Last 5 Events by Team Id, serta Last 15 Events by League Id dengan liga sasarannya yaitu Liga Premier Inggris.
2. Supaya pengguna dapat melihat cuaca, dilakukan **fetching API** pada API [OpenWeather](https://api.openweathermap.org/) kemudian mengambil data cuaca untuk asumsi lokasi yang selalu berada di **England**
3. Untuk menampilkan logo dan tanggal pertandingan, digunakan **picasso** dengan memanfaatkan data url logo team yang sudah diambil dari proses di atas.
4. Untuk mekanisme **cache**, dalam setiap kali proses *fetch API* data yang didapatkan akan disimpan pada sebuah database(*bolasepak_database*) yang memiliki empat buah *Entity* yaitu **Matchnext, Matchpast, Subscription dan Team**. Kemudian, ketika tidak ada koneksi internet, maka data yang ditampilkan akan diambil melalui database, dan ketika ada koneksi internet maka data yang ditampilkan merupakan data langsung dari proses *fetch API*
5. Untuk mekanisme **subscribe**, menggunakan bantuan satu tabel di database yaitu tabel *Subscription* di mana tabel tersebut menyimpan **ID_Team** mana saja yang di-subscribe oleh pengguna.
6. Untuk mekanisme **notifikasi**, menggunakan bantuan mekanisme *subscribe* termasuk tabel *susbcription*. Ketika pengguna melakukan *subscription* pada suatu tim tertentu, maka untuk setiap ID_Team yang ada pada tabel Subscription, akan dilakukan *fetch API* pada API thesportsdb untuk mendapatkan jadwal tim tersebut, dan jika jadwal bermain tim tersebut **kurang dari sama dengan 10 hari** maka akan diberikan notifikasi untuk pengguna
7. Untuk mekanisme **pencarian secara real-time** digunakan listener pada search untuk melakukan filter setiap query change
8. Untuk mekanisme **steps** digunakan accelerometer, digunakan foreground service agar langkah tetap diukur saat aplikasi di tutup. **notifikasi** tetap ada selama foreground service berjalan
9. Untuk Team detail digunakan Tablayout dan Fragment untuk masing - masing section pada tab
10. Untuk List of Match digunakan Recycler View dan CardView sebagai tempat untuk menempelkan keterangan pertandingan.
  

## Library
1. Android Room - digunakan untuk *wrap* SQLite. 
Android Room digunakan karena merupakan *best practices* dalam penggunaan SQLite pada androidx, serta kemudahannya untuk digunakan dengan memanfaatkan *worker thread* sehingga tidak mengganggu jalannya UI
2. Volley - digunakan untuk melakukan *fetch API* . Alasan lain penggunaan liblary Volley adalah karena liblary ini memiliki fitur untuk meng-cache request secara efektif dan management memory yang efektif juga.
3. Picasso - digunakan untuk menampilkan logo klub. Alasan penggunaannya adalah karena liblary ini sangat powerful dalam pengunduhan image/gambar.

  

## Screenshot
![111799](https://user-images.githubusercontent.com/38171936/76628664-ab6c5400-656f-11ea-916b-6ffd919d6ff1.jpg)
![2](https://user-images.githubusercontent.com/38171936/76620015-d7330e00-655e-11ea-9c95-b067cef111be.PNG)
![3](https://user-images.githubusercontent.com/38171936/76620018-d7cba480-655e-11ea-8c50-432027e29111.PNG)
![Capture](https://user-images.githubusercontent.com/38171936/76620016-d7330e00-655e-11ea-863d-a3c02f8c023b.PNG)
![5](https://user-images.githubusercontent.com/38171936/76620019-d8643b00-655e-11ea-9295-765d206c040d.PNG)
![111800](https://user-images.githubusercontent.com/38171936/76628666-ad361780-656f-11ea-8139-eb08229e7cd0.jpg)