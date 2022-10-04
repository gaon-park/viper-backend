package com.maple.viper.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.maple.viper.dto.response.GetCharacterInfoByAccountIDResponse
import com.maple.viper.exception.ViperException
import org.json.JSONObject
import org.json.XML

class JsonUtil {
    companion object {
        fun convertXmlToJson(xml: String?) = XML.toJSONObject(xml) ?: throw ViperException("no data")
    }

    @Suppress("SwallowedException")
    fun getDataFromNexonJson(jsonObject: JSONObject): GetCharacterInfoByAccountIDResponse = try {
        ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .readValue(getUsefulData(jsonObject).toString(), GetCharacterInfoByAccountIDResponse::class.java)
    } catch (e: JsonProcessingException) {
        throw ViperException(e.message ?: "json mapping 중 에러 발생")
    }

    private fun getUsefulData(jsonObject: JSONObject): JSONObject =
        jsonObject
            .getJSONObject("soap:Envelope")
            .getJSONObject("soap:Body")
            .getJSONObject("GetCharacterInfoByAccountIDResponse")
            .getJSONObject("GetCharacterInfoByAccountIDResult")
            .getJSONObject("diffgr:diffgram")
            .getJSONObject("NewDataSet")
            .getJSONObject("UserInfo")
}
