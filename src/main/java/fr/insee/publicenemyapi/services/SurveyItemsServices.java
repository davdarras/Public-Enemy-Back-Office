package fr.insee.publicenemyapi.services;

import fr.insee.publicenemyapi.PublicEnemyApiApplication;
import fr.insee.publicenemyapi.dao.impl.jpa.DataDaoJpaImpl;
import fr.insee.publicenemyapi.dao.impl.jpa.MetadataDaoJpaImpl;
import fr.insee.publicenemyapi.dao.impl.jpa.ModelDaoJpaImpl;
import fr.insee.publicenemyapi.exceptions.PublicEnemyException;
import fr.insee.publicenemyapi.model.SurveyData;
import fr.insee.publicenemyapi.model.SurveyMetadata;
import fr.insee.publicenemyapi.model.SurveyModel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class SurveyItemsServices {
    private static final Logger LOGGER = LoggerFactory.getLogger(SurveyItemsServices.class);
    @Autowired
    ModelDaoJpaImpl modelDao;
    @Autowired
    DataDaoJpaImpl dataDao;
    @Autowired
    MetadataDaoJpaImpl metadataDao;

    public void persistSurveyItems(String survey, MultipartFile model, MultipartFile data, MultipartFile metadata, String mode) throws SQLException, IOException, ParseException {

        InputStream isModel = model.getInputStream();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonModel = (JSONObject) jsonParser.parse(
                new InputStreamReader(isModel, StandardCharsets.UTF_8));

        InputStream isData = data.getInputStream();
        JSONObject jsonData = (JSONObject) jsonParser.parse(
                new InputStreamReader(isData, StandardCharsets.UTF_8));

        JSONObject jsonMetadata = null;
        if (null != metadata) {
            InputStream isMetadata = metadata.getInputStream();

            jsonMetadata = (JSONObject) jsonParser.parse(
                    new InputStreamReader(isMetadata, StandardCharsets.UTF_8));
        }

        SurveyModel sModel = new SurveyModel(survey, jsonModel);
        SurveyData sData = new SurveyData(survey, jsonData);
        SurveyMetadata sMetadata = null;
        if (null != metadata) {
            sMetadata = new SurveyMetadata(survey, jsonMetadata);
        }

        try {
            if (modelDao.exist(survey)) {
                modelDao.updateModel(sModel);
            } else {
                modelDao.createModel(sModel);
            }
        } catch (Exception e) {
            LOGGER.warn("error when persisting model json");
            LOGGER.warn(e.getMessage());
            throw new PublicEnemyException("error on model json persistence", e);
        }
        try {
            if (dataDao.exist(survey)) {
                dataDao.updateData(sData);
            } else {
                dataDao.createData(sData);
            }
        } catch (Exception e) {
            LOGGER.warn("error when persisting data json");
            LOGGER.warn(e.getMessage());
            throw new PublicEnemyException("error on data json persistence", e);
        }

        try {
            if (null != metadata && mode == "cawi" && metadataDao.exist(survey)) {
                metadataDao.updateMetadata(sMetadata);
            } else {
                if (null != metadata) {
                    metadataDao.createMetadata(sMetadata);
                }
            }
        } catch (Exception e) {
            LOGGER.warn("error when persisting metadata json");
            LOGGER.warn(e.getMessage());
            throw new PublicEnemyException("error on moetadata json persistence", e);
        }


    }

    public String buildVisualizationUrl(String domain, String survey, String mode) {
        String apiDomainUrl = null;
        try (InputStream input = PublicEnemyApiApplication.class.getClassLoader()
                .getResourceAsStream("application.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);
            apiDomainUrl = prop.getProperty("fr.insee.publicenemyapi.services.domain.url");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String url = domain + "?questionnaire={modelUrl}&data={dataUrl}";
        if (mode == "cawi") {
            url = url.concat("&metadata={metadataUrl}");
        }
// URI (URL) parameters
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("modelUrl", apiDomainUrl + "/survey/" + survey + "/model");
        urlParams.put("dataUrl", apiDomainUrl + "/survey/" + survey + "/data");
        if (mode == "cawi") {
            urlParams.put("metadataUrl", apiDomainUrl + "/survey/" + survey + "/metadata");

        }

        URI uri = UriComponentsBuilder.fromUriString(url)
                .buildAndExpand(urlParams)
                .toUri();
        return uri.toString();
    }
}
