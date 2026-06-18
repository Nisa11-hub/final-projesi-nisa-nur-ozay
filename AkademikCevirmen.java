/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package akademikcevirmen;

import java.util.Scanner;

/**
 *
 * @author Nisa
 */
public class AkademikCevirmen {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("--- BANA BASITCE ANLAT: YEREL AKADEMIK CEVIRMEN ---");
        System.out.println("Lutfen basitlestirilmesini istediginiz akademik cumleyi girin: ");

        String kullaniciMetni = input.nextLine();

        System.out.println("\nHarika! Metniniz sisteme alindi.");
        System.out.println("Simdi yerel modellere (Llama 3 ve Gemma) arka planda gonderiliyor...\n");

        // --- LLAMA 3 TESTI ---
        System.out.println(">>> Model 1: Llama 3 analiz ediyor (Lutfen bekleyin)...");
        long baslangicLlama = System.currentTimeMillis();
        String cevapLlama = modeldenCevapAl("llama3", kullaniciMetni);
        long bitisLlama = System.currentTimeMillis();
        System.out.println("Llama 3 Cevabi: " + cevapLlama);
        System.out.println("Llama 3 Islem Suresi: " + (bitisLlama - baslangicLlama) + " ms\n");

        // --- GEMMA TESTI ---
        System.out.println(">>> Model 2: Gemma analiz ediyor (Lutfen bekleyin)...");
        long baslangicGemma = System.currentTimeMillis();
        String cevapGemma = modeldenCevapAl("gemma:2b", kullaniciMetni);
        long bitisGemma = System.currentTimeMillis();
        System.out.println("Gemma Cevabi: " + cevapGemma);
        System.out.println("Gemma Islem Suresi: " + (bitisGemma - baslangicGemma) + " ms\n");
        
        System.out.println("Karsilastirma tamamlandi!");
    }

    // Ollama API HTTP baglanti metodumuz
    public static String modeldenCevapAl(String modelAdi, String metin) {
        try {
            java.net.URL url = new java.net.URL("http://localhost:11434/api/generate");
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // JSON'u bozmamak icin metindeki tirnaklari temizliyoruz
            String temizMetin = metin.replace("\"", "'");
            String prompt = "Şu karmaşık metni günlük dilde ve kısaca açıkla: " + temizMetin;
            String jsonInputString = "{\"model\": \"" + modelAdi + "\", \"prompt\": \"" + prompt + "\", \"stream\": false}";

            try(java.io.OutputStream os = conn.getOutputStream()) {
                byte[] inputBytes = jsonInputString.getBytes("utf-8");
                os.write(inputBytes, 0, inputBytes.length);
            }

            java.util.Scanner apiScanner = new java.util.Scanner(conn.getInputStream(), "utf-8");
            StringBuilder response = new StringBuilder();
            while(apiScanner.hasNext()) {
                response.append(apiScanner.nextLine());
            }
            apiScanner.close();

            String resStr = response.toString();
            int baslangic = resStr.indexOf("\"response\":\"") + 12;
            int bitis = resStr.indexOf("\"", baslangic);
            if(baslangic > 11 && bitis > baslangic) {
                // Gelen cevabın icindeki kacis karakterlerini ekranda duzgun gorunmesi icin temizliyoruz
                return resStr.substring(baslangic, bitis).replace("\\n", "\n").replace("\\\"", "\"").replace("\\t", "\t");
            }

        } catch (Exception e) {
            return "Baglanti Hatasi: Lutfen Ollama'nin arka planda calistigindan emin olun. Detay: " + e.getMessage();
        }
        return "Cevap alinamadi.";
    }
}
    
    

