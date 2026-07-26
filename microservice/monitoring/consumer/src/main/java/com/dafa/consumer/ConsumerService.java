package com.dafa.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class ConsumerService {

    @Autowired
    private JavaMailSender mailSender;

    @RabbitListener(queues = "myQueue")
    public void receivedMessage(String text) {
        System.out.println("📩 Pesan diterima dari RabbitMQ: " + text);

        try {
            String cleanText = text.replace("Order(", "").replace("Order{", "").replace("}", "").replace(")", "");
            String[] data = cleanText.split(",");

            String id = data[0].split("=")[1].trim();
            String produkId = data[1].split("=")[1].trim();
            String pelangganId = data[2].split("=")[1].trim();
            String harga = data[3].split("=")[1].trim();
            String jumlah = data[4].split("=")[1].trim();
            String total = data[5].split("=")[1].trim();
            String tanggal = data[6].split("=")[1].replace("'", "").trim();
            String status = data[7].split("=")[1].replace("'", "").trim();

            sendEmail(id, produkId, pelangganId, harga, jumlah, total, tanggal, status);

        } catch (Exception e) {
            System.out.println(
                    "Gagal memproses teks pesan. Pastikan format dari Order Service sesuai. Error: " + e.getMessage());
        }
    }

    public void sendEmail(String id, String produkId, String pelangganId, String harga,
            String jumlah, String total, String tanggal, String status) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom("dafadaifullah54@gmail.com");
            helper.setTo("dafadaifullah72@gmail.com");
            helper.setSubject("Konfirmasi: Order Baru Masuk! (#" + id + ")");

            // HTML EMAIL
            String html = "<div style='font-family:Arial, sans-serif; color:#333;'>" +
                    "<h2 style='color:#2c3e50; border-bottom: 2px solid #5ce45e; padding-bottom: 10px;'>Anda Telah Order di dafa Store</h2>"
                    +
                    "<p>Berikut rincian order barang yang dipesan:</p>" +

                    "<table style='border-collapse:collapse;width:100%; text-align: left;'>" +
                    "<tr style='background:#f8f9fa; color:#333;'>" +
                    "<th style='padding:12px; border:1px solid #ddd;'>ID Order</th>" +
                    "<th style='padding:12px; border:1px solid #ddd;'>ID Produk</th>" +
                    "<th style='padding:12px; border:1px solid #ddd;'>ID Pelanggan</th>" +
                    "<th style='padding:12px; border:1px solid #ddd;'>Harga</th>" +
                    "<th style='padding:12px; border:1px solid #ddd;'>Jumlah</th>" +
                    "<th style='padding:12px; border:1px solid #ddd;'>Total</th>" +
                    "<th style='padding:12px; border:1px solid #ddd;'>Tanggal</th>" +
                    "<th style='padding:12px; border:1px solid #ddd;'>Status</th>" +
                    "</tr>" +
                    "<tr>" +
                    "<td style='padding:12px; border:1px solid #ddd;'>" + id + "</td>" +
                    "<td style='padding:12px; border:1px solid #ddd;'>" + produkId + "</td>" +
                    "<td style='padding:12px; border:1px solid #ddd;'>" + pelangganId + "</td>" +
                    "<td style='padding:12px; border:1px solid #ddd;'>Rp " + harga + "</td>" +
                    "<td style='padding:12px; border:1px solid #ddd;'>" + jumlah + "</td>" +
                    "<td style='padding:12px; border:1px solid #ddd; font-weight:bold;'>Rp " + total + "</td>" +
                    "<td style='padding:12px; border:1px solid #ddd;'>" + tanggal + "</td>" +
                    "<td style='padding:12px; border:1px solid #ddd; color:green; font-weight:bold;'>" + status
                    + "</td>" +
                    "</tr>" +
                    "</table>" +

                    "<br><br>" +
                    "<p>Terima kasih sudah order di dafa Store!<br>" +
                    "<strong>Teknik Komputer 2B</strong></p>" +
                    "<hr style='border: none; border-top: 1px solid #eee;' />" +
                    "<small style='color:#777;'>2026 All rights reserved</small>" +
                    "</div>";

            helper.setText(html, true);
            mailSender.send(mimeMessage);
            System.out.println(
                    "✅ Email berisi tabel order berhasil dikirim ke " + helper.getMimeMessage().getAllRecipients()[0]);

        } catch (Exception e) {
            System.out.println("❌ Error saat mengirim email: " + e.getMessage());
        }
    }
}