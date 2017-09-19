package hello;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class GreetingController {
	@Autowired
	private HttpServletRequest request;

	@GetMapping(value = { "/", "/login" })
	public String greetingForm(Model model) {
		model.addAttribute("roaster", new Roaster());
		return "welcome";
	}

	@GetMapping("/monthly")
	public String showCalendar(Model model) {
		model.addAttribute("roaster", new Roaster());
		return "monthly";
	}

	@GetMapping("/roaster")
	public String showRoaster(Model model) {
		model.addAttribute("roaster", new Roaster());
		return "roaster";
	}

	@GetMapping("/morningchecks")
	public String showMorningChecks(Model model) {
		model.addAttribute("morningchecks", new Roaster());
		return "morningchecks";
	}

	@GetMapping("/implinks")
	public String showLinks(Model model) {
	//	model.addAttribute(arg0)
		return "testing";
	}

	@PostMapping("/links")
	public void addLinks(@RequestParam("link") String link) {
		List<String> newLines = new ArrayList<>();
		String fileName = "C:\\upload\\test.txt";
		try {
			for (String line : Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8)) {
			   
			       newLines.add(line);
			   
			}
			newLines.add(link);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Files.write(Paths.get(fileName), newLines, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@PostMapping("/greeting")
	public @ResponseBody String greetingSubmit(@RequestParam("file") MultipartFile file,
			@RequestParam("date") String date, @RequestParam("activityname") String activity,
			@ModelAttribute("roaster") Roaster roaster, BindingResult result) {

		String name = file.getOriginalFilename();
		System.out.println("date ;" + date);
		// System.out.println("date1 ;" +date1);
		if (!file.isEmpty()) {

			try {
				byte[] bytes = file.getBytes();

				// Creating the directory to store file

				// File dir = new File(rootPath + File.separator + date);
				File dir = new File("C:\\upload\\Monthly" + date + "\\" + activity);
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath() + File.separator + activity + ".xls");
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				return "You successfully uploaded file=" + name;
				// return new ModelAndView("calendar");
			} catch (Exception e) {
				return "You failed to upload " + name + " => " + e.getMessage();
				// return new ModelAndView("calendar");
			}
		} else {
			return "You failed to upload " + name + " because the file was empty.";
			// return new ModelAndView("calendar");
		}
	}

	@GetMapping("/readFile")
	public @ResponseBody HttpEntity<byte[]> loadFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("date") String date, @RequestParam("activityname") String activity) throws IOException {

		String fullPath = "C:\\upload\\Monthly" + date + "\\" + activity + "\\" + activity + ".xls";
		File downloadFile = new File(fullPath);
		if (!downloadFile.exists()) {
			String errorMessage = "Sorry. The file you are looking for does not exist";
			System.out.println(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			// return;
		}
		 FileInputStream fileInputStream = new FileInputStream("");
		 //ResponseBuilder responseBuilder = Response.ok((Object) fileInputStream);
		
		byte[] document = FileCopyUtils.copyToByteArray(downloadFile);
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "vnd.ms-excel"));
		header.set("Content-Disposition", "inline; filename=" + downloadFile.getName());
		header.setContentLength(document.length);
		return new HttpEntity<byte[]>(document, header);

	}

	@PostMapping("/roaster")
	public @ResponseBody String roasterSaveFile(@RequestParam("file") MultipartFile file,
			@RequestParam("date") String date, @RequestParam("activityname") String activity,
			@ModelAttribute("roaster") Roaster roaster, BindingResult result) {

		String name = file.getOriginalFilename();
		System.out.println(date);
		if (!file.isEmpty()) {

			try {
				byte[] bytes = file.getBytes();

				// Creating the directory to store file

				// File dir = new File(rootPath + File.separator + date);
				File dir = new File("C:\\upload\\Roaster\\" + date + "\\" + activity);
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath() + File.separator + activity + ".xls");
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				return "You successfully uploaded file=" + name;
				// return new ModelAndView("calendar");
			} catch (Exception e) {
				return "You failed to upload " + name + " => " + e.getMessage();
				// return new ModelAndView("calendar");
			}
		} else {
			return "You failed to upload " + name + " because the file was empty.";
			// return new ModelAndView("calendar");
		}
	}

	@GetMapping("/readRoaster")
	public void readRoaster(HttpServletRequest request, HttpServletResponse response, @RequestParam("date") String date,
			@RequestParam("activityname") String activity) throws IOException {
		System.out.println("roaster date :" + date);
		String fullPath = "C:\\upload\\Roaster\\" + date + "\\" + activity + "\\" + activity + ".xls";
		File downloadFile = new File(fullPath);
		if (!downloadFile.exists()) {
			String errorMessage = "Sorry. The file you are looking for does not exist";
			System.out.println(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return;
		}
		FileInputStream inputStream = new FileInputStream(downloadFile);

		// get MIME type of the file
		// String mimeType = context.getMimeType(fullPath);
		// if (mimeType == null) {
		// // set to binary type if MIME mapping not found
		// mimeType = "application/octet-stream";
		// }
		// System.out.println("MIME type: " + mimeType);

		// set content attributes for the response
		response.setContentType("application/octet-stream");
		response.setContentLength((int) downloadFile.length());

		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		response.setHeader(headerKey, headerValue);

		// get output stream of the response
		OutputStream outStream = response.getOutputStream();

		byte[] buffer = new byte[1024];
		int bytesRead = -1;

		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		inputStream.close();
		outStream.close();

	}

	@PostMapping("/morningcheck")
	public @ResponseBody String mrngcheckSaveFile(@RequestParam("file") MultipartFile file,
			@RequestParam("date") String date, @RequestParam("activityname") String activity,
			@ModelAttribute("roaster") Roaster roaster, BindingResult result) {

		String name = file.getOriginalFilename();
		System.out.println(date);
		if (!file.isEmpty()) {

			try {
				byte[] bytes = file.getBytes();

				// Creating the directory to store file

				// File dir = new File(rootPath + File.separator + date);
				File dir = new File("C:\\upload\\MorningChecks\\" + date + "\\" + activity);
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath() + File.separator + activity + ".xls");
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				return "You successfully uploaded file=" + name;
				// return new ModelAndView("calendar");
			} catch (Exception e) {
				return "You failed to upload " + name + " => " + e.getMessage();
				// return new ModelAndView("calendar");
			}
		} else {
			return "You failed to upload " + name + " because the file was empty.";
			// return new ModelAndView("calendar");
		}
	}

	@GetMapping("/readChecks")
	public void readChecks(HttpServletRequest request, HttpServletResponse response, @RequestParam("date") String date,
			@RequestParam("activityname") String activity) throws IOException {

		String fullPath = "C:\\upload\\MorningChecks\\" + date + "\\" + activity + "\\" + activity + ".xls";
		File downloadFile = new File(fullPath);
		if (!downloadFile.exists()) {
			String errorMessage = "Sorry. The file you are looking for does not exist";
			System.out.println(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return;
		}
		FileInputStream inputStream = new FileInputStream(downloadFile);

		// get MIME type of the file
		// String mimeType = context.getMimeType(fullPath);
		// if (mimeType == null) {
		// // set to binary type if MIME mapping not found
		// mimeType = "application/octet-stream";
		// }
		// System.out.println("MIME type: " + mimeType);

		// set content attributes for the response
		response.setContentType("application/octet-stream");
		response.setContentLength((int) downloadFile.length());

		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		response.setHeader(headerKey, headerValue);

		// get output stream of the response
		OutputStream outStream = response.getOutputStream();

		byte[] buffer = new byte[1024];
		int bytesRead = -1;

		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		inputStream.close();
		outStream.close();

	}
}
