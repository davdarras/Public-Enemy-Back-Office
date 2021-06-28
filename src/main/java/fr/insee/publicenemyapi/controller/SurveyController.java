package fr.insee.publicenemyapi.controller;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import fr.insee.publicenemyapi.config.ApplicationContext;
import fr.insee.publicenemyapi.dao.DataDao;
import fr.insee.publicenemyapi.dao.MetadataDao;
import fr.insee.publicenemyapi.dao.ModelDao;
import fr.insee.publicenemyapi.dao.impl.jpa.DataDaoJpaImpl;
import fr.insee.publicenemyapi.dao.impl.jpa.MetadataDaoJpaImpl;
import fr.insee.publicenemyapi.dao.impl.jpa.ModelDaoJpaImpl;
import fr.insee.publicenemyapi.model.SurveyData;
import fr.insee.publicenemyapi.model.SurveyMetadata;
import fr.insee.publicenemyapi.model.SurveyModel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.insee.publicenemyapi.services.SurveyServices;
import org.springframework.web.util.UriComponentsBuilder;

@RestController

public class SurveyController {

	@Autowired
	SurveyServices qs;
	@Autowired
	ModelDaoJpaImpl modelDao;
	@Autowired
	DataDaoJpaImpl dataDao;
	@Autowired
	MetadataDaoJpaImpl metadataDao;

	@RequestMapping(value = "/survey-papi", produces = MediaType.APPLICATION_OCTET_STREAM, consumes = MediaType.MULTIPART_FORM_DATA, method = {
			RequestMethod.POST })
	public ResponseEntity<ByteArrayResource> sendPdf(@RequestParam("survey") String format,
			@RequestParam("data") MultipartFile data, @RequestParam("model") MultipartFile model,
			HttpServletRequest request) throws IOException {

		String modelFileName = model.getOriginalFilename();
		String dataFileName = data.getOriginalFilename();

		InputStream model2 = new BufferedInputStream(model.getInputStream());
		InputStream data2 = new BufferedInputStream(data.getInputStream());

		Path path = Paths.get(qs.publiPostage(model2, modelFileName, data2, dataFileName).getAbsolutePath());
		ByteArrayResource pdfFile = new ByteArrayResource(Files.readAllBytes(path));

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
				.contentLength(pdfFile.contentLength()).body(pdfFile);

	}

	@RequestMapping(value = "/perso-papi", produces = MediaType.APPLICATION_OCTET_STREAM, consumes = MediaType.MULTIPART_FORM_DATA, method = {
			RequestMethod.POST })
	public ResponseEntity<ByteArrayResource> sendPersoFile(@RequestParam("survey") String enquete,
			@RequestParam("model") MultipartFile model, HttpServletRequest request) throws Exception {

		String modelFileName = model.getOriginalFilename();

		InputStream model2 = new BufferedInputStream(model.getInputStream());

		Path path = Paths.get(qs.personalisationFile(model2, modelFileName).getAbsolutePath());
		ByteArrayResource xmlPersoFile = new ByteArrayResource(Files.readAllBytes(path));

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
				.contentLength(xmlPersoFile.contentLength()).body(xmlPersoFile);

	}

	@RequestMapping(value = "/perso-cawi-capi", produces = MediaType.APPLICATION_OCTET_STREAM, consumes = MediaType.MULTIPART_FORM_DATA, method = {
			RequestMethod.POST })
	public ResponseEntity<ByteArrayResource> sendPersoFileWeb(@RequestParam("survey") String enquete,
			@RequestParam("model") MultipartFile model, HttpServletRequest request) throws Exception {

		String modelFileName = model.getOriginalFilename();

		InputStream model2 = new BufferedInputStream(model.getInputStream());

		Path path = Paths.get(qs.personalisationFileWeb(model2, modelFileName).getAbsolutePath());
		ByteArrayResource xmlPersoFile = new ByteArrayResource(Files.readAllBytes(path));

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
				.contentLength(xmlPersoFile.contentLength()).body(xmlPersoFile);

	}

	@RequestMapping(value = "/survey-cawi-capi", consumes = MediaType.MULTIPART_FORM_DATA, method = { RequestMethod.POST })
	public @ResponseBody String sendUrl(@RequestParam("survey") String survey, @RequestParam("data") MultipartFile data,
			@RequestParam("model") MultipartFile model) throws Exception {

		String modelFileName = model.getOriginalFilename();
		String dataFileName = data.getOriginalFilename();

		InputStream model2 = new DataInputStream(model.getInputStream());
		InputStream data2 = new DataInputStream(data.getInputStream());

		return (qs.publiPostageWeb(survey, model2, modelFileName, data2, dataFileName));

	}

	@RequestMapping(value = "/survey-cawiv2", consumes = MediaType.MULTIPART_FORM_DATA, method = { RequestMethod.POST })
	public @ResponseBody String sendUrl(@RequestParam("survey") String survey, @RequestParam("data") MultipartFile data,
										@RequestParam("model") MultipartFile model, @RequestParam("metadata") MultipartFile metadata) throws Exception {


		InputStream isModel = model.getInputStream();
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonModel = (JSONObject)jsonParser.parse(
				new InputStreamReader(isModel, "UTF-8"));

		InputStream isData = data.getInputStream();
		JSONObject jsonData = (JSONObject)jsonParser.parse(
				new InputStreamReader(isData, "UTF-8"));

		JSONObject jsonMetadata=null;
		if(null!=metadata) {
			InputStream isMetadata = metadata.getInputStream();

			jsonMetadata = (JSONObject) jsonParser.parse(
					new InputStreamReader(isMetadata, "UTF-8"));
		}

		SurveyModel sModel =new SurveyModel(survey, jsonModel);
		SurveyData sData = new SurveyData(survey, jsonData);
		SurveyMetadata sMetadata=null;
		if(null!=metadata) {
			sMetadata = new SurveyMetadata(survey, jsonMetadata);
		}


		if(modelDao.exist(survey))
		{
			modelDao.updateModel(sModel);
		}else{
			modelDao.createModel(sModel);
		}

		if(dataDao.exist(survey))
		{
			dataDao.updateData(sData);
		}else{
			dataDao.createData(sData);
		}

		if(null!= metadata && metadataDao.exist(survey))
		{
			metadataDao.updateMetadata(sMetadata);
		}else{
			if(null!=metadata) {
				metadataDao.createMetadata(sMetadata);
			}
		}

		String url = "https://questionnaires-web-particuliers.developpement.insee.fr/visualize?questionnaire={modelUrl}&metadata={metadataUrl}&data={dataUrl}";

// URI (URL) parameters
		Map<String, String> urlParams = new HashMap<>();
		urlParams.put("modelUrl", "http://localhost:8080/survey/"+survey+"/model");
		urlParams.put("metadataUrl", "http://localhost:8080/survey/"+survey+"/metadata");
		urlParams.put("dataUrl", "http://localhost:8080/survey/"+survey+"/data");

		URI uri = UriComponentsBuilder.fromUriString(url)
				.buildAndExpand(urlParams)
				.toUri();

		return uri.toString();

	}

}
