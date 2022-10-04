package com.maple.viper.util

import com.maple.viper.exception.ViperException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets


class SoapUtil {

    /**
     * 대표캐릭터 정보 획득 soap api
     */
    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    fun getCharacterInfoByAccountID(accountId: Int): JSONObject {
        var result: String? = ""
        try {
            // send message
            val sendMessage = """<?xml version="1.0" encoding="utf-8"?>
<soap12:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap12="http://www.w3.org/2003/05/soap-envelope">
  <soap12:Body>
    <GetCharacterInfoByAccountID xmlns="https://api.maplestory.nexon.com/soap/">
      <AccountID>$accountId</AccountID>
    </GetCharacterInfoByAccountID>
  </soap12:Body>
</soap12:Envelope>"""

            val url = URL("https://api.maplestory.nexon.com/soap/maplestory.asmx")
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            con.doOutput = true
            con.requestMethod = "POST"

            // header
            con.addRequestProperty("Content-Type", "application/soap+xml; charset=utf-8")
            con.addRequestProperty("SOAPAction", "https://api.maplestory.nexon.com/soap/GetCharacterInfoByAccountID")
            con.addRequestProperty("Content-Length", sendMessage.toByteArray(StandardCharsets.UTF_8).size.toString())

            // body
            val writer = OutputStreamWriter(con.outputStream)
            writer.write(sendMessage)
            writer.flush()

            // response
            var inputLine: String? = null
            var responseStr: String? = ""
            val reader = BufferedReader(InputStreamReader(con.inputStream))
            while (reader.readLine().also { inputLine = it } != null) {
                responseStr += inputLine
            }
            result = responseStr
            reader.close()
            writer.close()
        } catch (e: Exception) {
            throw ViperException(e.message ?: "soap api 실행중 오류")
        }
        return JsonUtil.convertXmlToJson(result)
    }
}
