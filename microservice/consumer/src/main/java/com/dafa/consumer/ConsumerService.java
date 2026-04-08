package com.dafa.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class ConsumerService {

    @Autowired
    private JavaMailSender mailSender;

    @RabbitListener(queues = "myQueue")
    public void receivedMessage(String text) {
        System.out.println("📩 Received: " + text);
        sendEmail(text);
    }

    public void sendEmail(String text) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom("dafadaifullah54@gmail.com");
            helper.setTo("ashun7328@gmail.com");
            helper.setSubject("Order Baru Masuk");

            text = text.replace("Order{", "").replace("}", "");
            String[] data = text.split(",");

            String id = data[0].split("=")[1];
            String id_produk = data[1].split("=")[1];
            String id_pelanggan = data[2].split("=")[1];
            String harga = data[3].split("=")[1];
            String jumlah = data[4].split("=")[1];
            String total = data[5].split("=")[1];
            String tanggal = data[6].split("=")[1].replace("'", "");
            String status = data[7].split("=")[1].replace("'", "");

            // ==========================
            // HTML EMAIL
            // ==========================
            String html = "<div style='font-family:Arial'>" +

                    "<h2 style='color:green'>Terima kasih sudah mengorder</h2>" +
                    "<p>Berikut rincian order barang yang dipesan:</p>" +

                    "<table style='border-collapse:collapse;width:100%'>" +

                    "<tr style='background:#f2f2f2'>" +
                    "<th style='padding:8px;border:1px solid #ccc;'>ID</th>" +
                    "<th style='padding:8px;border:1px solid #ccc;'>ID Produk</th>" +
                    "<th style='padding:8px;border:1px solid #ccc;'>ID Pelanggan</th>" +
                    "<th style='padding:8px;border:1px solid #ccc;'>Harga</th>" +
                    "<th style='padding:8px;border:1px solid #ccc;'>Jumlah</th>" +
                    "<th style='padding:8px;border:1px solid #ccc;'>Total</th>" +
                    "<th style='padding:8px;border:1px solid #ccc;'>Tanggal</th>" +
                    "<th style='padding:8px;border:1px solid #ccc;'>Status</th>" +
                    "</tr>" +

                    "<tr>" +
                    "<td style='padding:8px;border:1px solid #ccc;'>" + id + "</td>" +
                    "<td style='padding:8px;border:1px solid #ccc;'>" + id_produk + "</td>" +
                    "<td style='padding:8px;border:1px solid #ccc;'>" + id_pelanggan + "</td>" +
                    "<td style='padding:8px;border:1px solid #ccc;'>" + harga + "</td>" +
                    "<td style='padding:8px;border:1px solid #ccc;'>" + jumlah + "</td>" +
                    "<td style='padding:8px;border:1px solid #ccc;'>" + total + "</td>" +
                    "<td style='padding:8px;border:1px solid #ccc;'>" + tanggal + "</td>" +
                    "<td style='padding:8px;border:1px solid #ccc;'>" + status + "</td>" +
                    "</tr>" +

                    "</table>" +

                    "<br><br>" +

                    "<p>Salam,<br>" +
                    "Dafa Daifullagi<br>" +
                    "2401081004<br>" +
                    "Teknik Komputer 2B</p>" +

                    "<br>" +
                    "<small>All rights reserved</small>" +

                    "</div>";

            helper.setText(html, true);

            mailSender.send(mimeMessage);

            System.out.println("Email tabel berhasil dikirim");

        } catch (Exception e) {
            System.out.println("Error email: " + e.getMessage());
        }
    }
}