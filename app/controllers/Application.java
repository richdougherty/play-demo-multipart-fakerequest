package controllers;

import java.io.File;
import org.apache.commons.io.FileUtils;
import play.*;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;

import views.html.*;

public class Application extends Controller {

    public static Result index() throws Exception {
      MultipartFormData form = request().body().asMultipartFormData();
      MultipartFormData.FilePart filePart = form.getFile("key");
      File file = filePart.getFile();
      String fileContent = FileUtils.readFileToString(file, "utf-8");
      return ok(fileContent);
    }

}
