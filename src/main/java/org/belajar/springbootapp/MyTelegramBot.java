package org.belajar.springbootapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Hello world!
 *
 */
public class MyTelegramBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();
            List<String> dataFromUser = parseMessageAndCommand(messageText);
            String commands = getCommand(dataFromUser.get(0));
            List<String> result = requestApiBanking(commands, dataFromUser.get(1));
            if (result.isEmpty()) {
                try {
                    SendMessage message = SendMessage.builder()
                            .chatId(chatId)
                            .text("Data Akun Tidak Ditemukan")
                            .build();
                    execute(message);
                } catch (TelegramApiException ignore) {

                }
            }
            String accountNumber = "No Rekening\t\t\t: <code>" + result.get(0) + "</code>\n";
            String bankName = "Nama Bank\t\t: <code>" + setBankData(result.get(1)) + "</code>\n";
            String accountName = "Nama Rekening\t: <code>" + result.get(2) + "</code>";

            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(bankName + accountNumber + accountName)
                    .parseMode("HTML")
                    .build();


            try {
                execute(message);
            } catch (TelegramApiException e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public String getBotUsername() {
        return "araaasharebot";
    }

    @Override
    public String getBotToken() {
        return "7296211811:AAEJZzpmNqywhHfL8UO7F5NShJVs_1mq8Dg";
    }

    public static List<String> parseMessageAndCommand(String message) {
        List<String> accountNumber = new ArrayList<>();
        String[] messageText = message.split(" ");
        Collections.addAll(accountNumber, messageText);
        if (accountNumber.size() == 2) {
            return accountNumber;
        }
        return Collections.emptyList();
    }
    public String getCommand(String commands){
        return commands.replace("/", "");
    }

    public List<String> requestApiBanking(String bank, String account) {
        String url = "https://cekrekening-api.belibayar.online/api/v1/account-inquiry";
        TelegramBotRequestRecieve telegramBotRequestRecieve = new TelegramBotRequestRecieve(bank, account);
        Gson gson = new Gson();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(telegramBotRequestRecieve), StandardCharsets.UTF_8))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Gson responseGson = new GsonBuilder().create();
                ResponseFromApi responseFromApi = responseGson.fromJson(response.body(), ResponseFromApi.class);
                System.out.println(responseFromApi.data.account_number);
                return List.of(responseFromApi.data.account_number, responseFromApi.data.account_bank, responseFromApi.data.account_holder);
            }
        } catch (InterruptedException | IOException e) {
            return Collections.emptyList();
        }

    return Collections.emptyList();
    }
    public static String setBankData(String bankData) {
        switch (bankData.toLowerCase()){
            case "harda":
                return "Allo Bank/Bank Harda Internasional";
            case "anz" :
                return "ANZ Indonesia";
            case "aceh":
                return "Bank Aceh Syariah";
            case "aladin":
                return "Bank Aladin Syariah";
            case "amar":
                return "Bank Amar Indonesia";
            case "anatardaerah":
                return "Bank AntarDaerah";
            case "artha":
                return "Bank Artha Graha Internasional";
            case "bengkulu":
                return "Bank Bengkulu";
            case "daerah_istimewa":
                return "Bank BPD DIY";
            case "daerah_istimewa_syr":
                return "Bank BPD DIY Syariah";
            case "btpn_syr":
                return "Bank BTPN Syariah";
            case "bukopin_syr":
                return "Bank Bukopin Syariah";
            case "bumi_artha":
                return "Bank Bumi Artha";
            case "capital":
                return "Bank Capital Indonesia";
            case "bca":
                return "Bank Central Asia";
            case "ccb":
                return "Bank China Construction Bank Indonesia";
            case "cnb":
                return "Bank CNB(Centratama Nasional Bank)";
            case "danamon":
                return "Bank Danamon & Danamon Syariah";
            case "dinar":
                return "Bank Dinar Indonesia";
            case "dki":
                return "Bank DKI";
            case "dki_syr":
                return "Bank DKI Syariah";
            case "ganesha":
                return "Bank Ganesha";
            case "agris":
                return "Bank IBK Indonesia";
            case "ina_perdana":
                return "Bank Ina Perdana";
            case "index_selindo":
                return "Bank Index Selindo";
            case "artos_syr":
                return "Bank Jago Syariah";
            case "jambi":
                return "Bank Jambi";
            case "jambi_syr":
                return "Bank Jambi Syariah";
            case "jasa_jakarta":
                return "Bank Jasa Jakarta";
            case "jawa_tengah":
                return "Bank Jateng";
            case "jawa_tengah_syr":
                return "Bank Jateng Syariah";
            case "jawa_timur":
                return "Bank Jatim";
            case "jawa_timur_syr":
                return "Bank Jatim Syariah";
            case "kalimantan_barat":
                return "Bank Kalbar";
            case "kalimantan_barat_syariah":
                return "Bank Kalbar Syariah";
            case "kalimantan_selatan":
                return "Bank Kalimantan Selatan";
            case "kalimantan_selatan_syr":
                return "Bank Kalimantan Selatan Syariah";
            case "kalimantan_tengah":
                return "Bank Kalimantan Tengah";
            case "kalimantan_timur":
                return "Bank Kalimantan Timur";
            case "kalimantan_timur_syr":
                return "Bank Kalimantan Timur Syariah";
            case "lampung":
                return "Bank Lampung";
            case "mandiri":
                return "Bank Mandiri";
            case "mantap":
                return "Bank Mantap(Mandiri Taspen)";
            case "maspion":
                return "Bank Maspion";
            case "mayapada":
                return "Bank Mayapada";
            case "mayora":
                return "Bank Mayora";
            case "mega":
                return "Bank Mega";
            case "mega_syr":
                return "Bank mega Syariah";
            case "mestika_dharma":
                return "Bank Mestika Dharma";
            case "mizuho":
                return "Bank Mizuho Indonesia";
            case "mas":
                return "Bank Multi Arta Sentosa(MAS)";
            case "mutiara":
                return "Bank Mutiara";
            case "sumatera_barat":
                return "Bank Nagari";
            case "sumatera_barat_syr":
                return "Bank Nagari Syariah";
            case "nusa_tenggara_barat":
                return "Bank NTB Syariah";
            case "nusa_tenggara_timur":
                return "Bank NTT";
            case "nusantara_parahyanan":
                return "Bank Nusantara Prahyangan";
            case "ocbc":
                return "Bank OCBC NISP";
            case "ocbc_syr":
                return "Bank OCBC NISP Syariah";
            case "america_na":
                return "Bank America NA";
            case "boc":
                return "Bank Of China (Hong Kong) Limited";
            case "india":
                return "Bank India Indonesia";
            case "tokyo":
                return "Bank of Tokyo Mitsubishi UFJ";
            case "papua":
                return "Bank Papua";
            case "prima":
                return "Bank Prima Master";
            case "bri":
                return "Bank Rakyat Indonesia";
            case "riau_dan_kepri":
                return "Bank Riau Kepri";
            case "sahabat_sampoerna":
                return "Bank Sahabat Sampoerna";
            case "sinhan":
                return "Bank Sinhan Indonesia";
            case "sinarmas":
                return "Bank Sinarmas";
            case "sinarmas_syr":
                return "Bank Sinarmas Syariah";
            case "sulselbar":
                return "Bank Sulselbar";
            case "sulselbar_syr":
                return "Bank Sulselbar Syariah";
            case "sulawesi":
                return "Bank Sulawesi";
            case "sulawesi_tenggara":
                return "Bank Sultra";
            case "sulut":
                return "Bank SulutGo";
            case "sumsel_dan_babel":
                return "Bank Sumsel Babel";
            case "sumsel_dan_babel_syr":
                return "Bank Sumsel Babel Syariah";
            case "sumut":
                return "Bank Sumut";
            case "sumut_syr":
                return "Bank Sumut Syariah";
            case "resona_perdania":
                return "Bank Resona Perdania";
            case "victoria_internasioal":
                return "Bank Victoria Internasional";
            case "victorial_syr":
                return "Bank Victoria Syariah";
            case "woori":
                return "Bank Woori Saudara";
            case "bca_syr":
                return "BCA(Bank Central Asia) Syariah";
            case "bjb":
                return "BJB";
            case "bjb_syr":
                return "BJB Syariah";
            case "royal":
                return "Blu/BCA Digiral";
            case "bni":
                return "BNI (Bank negara Indonesia)";
            case "bnp_paribas":
                return "BNP Paribas Indonesia";
            case "bali":
                return "BPD Bali";
            case "banten":
                return "BPD Banten";
            case "eka":
                return "BPR Eka(Bank Eka)";
            case "agroniaga":
                return "BRI Agroniaga";
            case "bsm":
                return "Bank Syariah Indonesia";
            case "btn":
                return "BTN";
            case "btn_syr":
                return "BTN Syariah";
            case "tabungan_pensiunan_nasional":
                return "BTPN";
            case "cimb":
                return "CIMB Niaga & CIMB Niaga Syariah";
            case "citibank":
                return "Citibank";
            case "commonwealth":
                return "Commonwealth Bank";
            case "chinatrust":
                return "CTBC (Chinathrust) Indonesia";
            case "dbs":
                return "DBS Indonesia";
            case "hsbc":
                return "HSBC Indonesia";
            case "icbc":
                return "ICBC Indonesia";
            case "artos":
                return "Jago/Artos";
            case "hana":
                return "Line Bank/KEB Hana";
            case "bii":
                return "Maybank Indonesia";
            case "bii_syr":
                return "Maybank Syariah";
            case "mnc_internasional":
                return "Motion/MNC Bank";
            case "muamalat":
                return "Muamalat";
            case "yudha_bakti":
                return "Neo Commerce/ Yudha Bhakti";
            case "nationalnobu":
                return "Nobu (Nationalnobu) Bank";
            case "panin":
                return "Panin Bank";
            case "panin_syr":
                return "Panin Syariah";
            case "permata":
                return "Permata";
            case "permata_syr":
                return "Permata Syariah";
            case "qnb_kesawan":
                return "QNB Indonesia";
            case "rabobank":
                return "Rabobank International Indonesia";
            case "sbi_indonesia":
                return "SBI Indonesia";
            case "kesejahteraan_ekonomi":
                return "Seabank/Bank BKE";
            case "standard_chartered":
                return "Standard Chartered Bank";
            case "super_bank":
                return "SuperBank";
            case "uob":
                return "TMRW/UOB";
            case "bukopin":
                return "Wokee/Bukopin";
            case "krom":
                return "Krom bank Indonesia";
            case "dana":
                return "DANA";
            case "gopay":
                return "GoPay";
            case "linkaja":
                return "LinkAja";
            case "ovo":
                return "OVO";
            case "shopeepay":
                return "ShopeePay";
            default:
                return bankData;

        }
    }
}

