package com.legs.appsforaa;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AboutDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        builder.setTitle(getString(R.string.About) + " - AAINS");
        builder.setIcon(R.mipmap.ic_launcher);

        String aboutHtml =
            "<h3>AAINS</h3>" +
            "<b>Android Auto Apps Installer</b><br>" +
            "Sürüm: <b>2.2-unlimited</b><br><br>" +
            "<b>Geliştirici:</b> NTRooT<br>" +
            "<b>E-posta:</b> <a href=\"mailto:ntroot@gmail.com\">ntroot@gmail.com</a><br><br>" +
            "<b>Teşekkür &amp; Fork Bilgisi:</b><br>" +
            "Bu uygulama, <b>Gabriele Rizzo (shmykelsa)</b> tarafından geliştirilen açık kaynaklı <b>AAAD (Android Auto Apps Downloader)</b> projesinden fork edilerek geliştirilmiştir. " +
            "Android Auto ekosistemine sunduğu öncü katkılar ve bu projeye ilham veren açık kaynak temelleri için orijinal AAAD geliştiricilerine en içten teşekkürlerimizi sunarız.<br><br>" +
            "<b>Desteklenen Uygulamaların Geliştiricileri:</b><br>" +
            "• Annexhack - Screen2Auto, AAStream<br>" +
            "• Andrey Pavlenko - Fermata Auto<br>" +
            "• Slashmax - AA Mirror &amp; AA Mirror Plus<br>" +
            "• Kiran Kumar, Eselter, Kristakos - CarStream<br>" +
            "• agronick - AA Torque<br>" +
            "• jilleb (chillout) - Performance Monitor (MQB)<br>" +
            "• kododake - AA Browser<br>" +
            "• FrankieNL - Nav2Contacts<br>" +
            "• ns130291 - Widgets for Android Auto<br>" +
            "• Martoreto - AA Passenger<br><br>" +
            "<b>Altyapı &amp; Kütüphaneler:</b><br>" +
            "• Material 3 Components (Google)<br>" +
            "• Shizuku API (Rikka)<br>" +
            "• OkHttp (Square)";

        builder.setMessage(Html.fromHtml(aboutHtml, Html.FROM_HTML_MODE_COMPACT));
        builder.setCancelable(true);

        builder.setPositiveButton(getString(android.R.string.ok), (dialog, which) -> {});

        builder.setNeutralButton("İletişim", (dialog, which) -> {
            try {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:ntroot@gmail.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "AAINS - Destek / Geri Bildirim");
                startActivity(Intent.createChooser(emailIntent, "E-posta Gönder"));
            } catch (Exception e) {
                Toast.makeText(requireContext(), "E-posta uygulaması açılamadı: ntroot@gmail.com", Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        TextView messageView = alert.findViewById(android.R.id.message);
        if (messageView != null) {
            messageView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        return alert;
    }
}
