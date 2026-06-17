# Bana Basitçe Anlat: Yerel Akademik Çevirmen

## Projenin Adı
Bana Basitçe Anlat: Yerel Akademik Çevirmen

## Problem Tanımı
Araştırmacıların ve veri bilimi öğrencilerinin karmaşık akademik metinleri incelerken veri gizliliğini (KVKK vb.) koruma ihtiyacı. İnternet bağlantısı gerektiren bulut tabanlı API'ler (ChatGPT vb.) yerine, tamamen yerel makinede çalışan bir sistemle gizli veya henüz yayımlanmamış akademik çalışmaların güvenle basitleştirilerek çevrilmesi.

## Hedef Kullanıcı
Lisans ve lisansüstü öğrencileri, bağımsız araştırmacılar ve veri gizliliğine önem veren akademisyenler.

## Çözümün Kısa Açıklaması
Bu proje, Java tabanlı bir konsol uygulaması aracılığıyla kullanıcının girdiği karmaşık bir akademik metni alır ve bilgisayarda yerel olarak çalışan Ollama altyapısındaki iki farklı büyük dil modeline (Llama 3 ve Gemma) HTTP istekleri üzerinden gönderir. Modeller metni işler ve sonuçlar, işlem süreleriyle birlikte karşılaştırmalı olarak kullanıcıya sunulur.

## Kullanılan Teknolojiler
*   **Java (JDK):** Uygulamanın ana iskeleti, kullanıcı veri girişi (Scanner) ve API haberleşmesi (HTTPURLConnection).
*   **Ollama:** Yerel yapay zeka modellerini cihazda çalıştırmak için motor.
*   **Llama 3 (8B) & Gemma (2B):** Metin analizi ve basitleştirme işlemlerini gerçekleştiren açık kaynaklı büyük dil modelleri.

## Sistem Mimarisi veya İş Akışı
1. Kullanıcı, Java konsolu üzerinden `Scanner` ile akademik bir cümleyi sisteme girer.
2. Java programı, bu girdiyi bir JSON formatına dönüştürür.
3. Arka planda `http://localhost:11434/api/generate` adresine POST isteği atılarak metin önce Llama 3'e, ardından Gemma'ya iletilir.
4. Modeller çevrimdışı ortamda çıkarım (inference) yapar.
5. Dönen JSON formatındaki yanıtlar ayrıştırılır ve işlem süresi (milisaniye) hesaplanarak ekrana yazdırılır.

## Kurulum Adımları
1. [Ollama.com](https://ollama.com) adresinden Ollama'yı bilgisayarınıza kurun.
2. Terminal (Komut İstemi) üzerinden sırasıyla `ollama run llama3` ve `ollama run gemma:2b` komutlarını çalıştırarak modelleri indirin.
3. Bu depodaki Java dosyasını (AkademikCevirmen.java) bilgisayarınıza indirin.
4. Tercih ettiğiniz bir Java IDE'sinde (NetBeans, Eclipse vb.) projeyi açın. (Türkçe karakter sorunu yaşamamak için Run > VM Options kısmına `-Dfile.encoding=UTF-8` eklenmesi tavsiye edilir).

## Kullanım Biçimi
Programı derleyip çalıştırdığınızda konsol sizden bir metin isteyecektir. Metni girip Enter tuşuna bastığınızda, sistem metni sırayla modellere gönderecek ve sonuçları işlem süreleriyle birlikte ekranda listeleyecektir.

## Örnek Ekran Görüntüleri
*(Proje klasöründeki ekran görüntüleri incelenebilir)*
* `gemma-hata-ekrani.png` : Küçük modelin Türkçe yetersizliğini gösteren test ekranı.
* `llama-indirme.png` : Modelin Ollama üzerinden indirilme aşaması.
* `kod-arayuzu.png` : Projenin temel Java yapısı.
* `final-test-sonucu.png` : İki modelin aynı metin üzerindeki hız ve çıktı performansı karşılaştırılması.
  
## Test Sonuçları
Geleneksel Transformer ve State Space Modelleri (SSM) ile ilgili karmaşık bir akademik cümle teste tabi tutulmuştur.
*   **Llama 3:** İşlemi ~28.506 ms (28.5 saniye) sürede tamamladı. Cümleyi anlamsal olarak çok daha iyi kavramış olsa da, karmaşıklığı çözmek için çıktıyı İngilizceye çevirme ("Let me break it down...") eğilimi göstermiştir.
*   **Gemma 2B:** İşlemi ~11.756 ms (11.7 saniye) sürede tamamladı. Çok daha hızlı olmasına karşın, model boyutunun küçüklüğü ve Türkçe bağlam penceresinin darlığı sebebiyle konsol kodlamasında bozulmalar yaşamış ve metni toparlayamamıştır. 

## Bilinen Sınırlılıklar
*   Küçük parametreli yerel modellerin Türkçe veri seti eksikliği nedeniyle anlamsal bütünlükte sorun yaşaması.
*   IDE alt konsollarındaki standart UTF-8 uyumsuzluklarının ASCII dışı karakterlerde (Türkçe) görsel bozulmalara yol açabilmesi.
*   Yerel cihazın donanım kapasitesine bağlı olarak büyük modellerin (Llama 3) çıkarım sürelerinin uzaması.

## Gelecekte Yapılabilecek Geliştirmeler
Sisteme **RAG (Retrieval-Augmented Generation)** mimarisi entegre edilebilir. Yerel bir vektör veritabanına sadece Türkçe akademik makaleler yüklenerek modellerin bu veritabanından bağlam çekmesi sağlanabilir. Böylece halüsinasyon oranı düşürülüp Türkçe çıktı kalitesi artırılabilir.

## Yapay Zekâ Araçlarının Kullanımı
Bu projedeki kodların yazım sürecinde değil; `Scanner` ile UTF-8 uyumsuzluklarının giderilmesi, HTTP isteklerindeki JSON ayrıştırma hatalarının çözümü ve modeller arası milisaniye cinsinden performans analizlerinin raporlanması gibi teknik sorunların giderilmesinde yapay zeka araçlarından asistan olarak faydalanılmıştır.
