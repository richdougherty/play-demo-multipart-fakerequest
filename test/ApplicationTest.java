import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import java.io.File;
import org.apache.commons.io.FileUtils;
import play.api.libs.Files.TemporaryFile;
import play.api.mvc.AnyContent;
import play.api.mvc.AnyContentAsMultipartFormData;
import play.api.mvc.MultipartFormData;
import play.api.mvc.MultipartFormData.FilePart;
import play.libs.Scala;

public class ApplicationTest {

    @Test
    public void fakeMultiPartRequest() throws Exception {

        // Based on Kris's answer http://stackoverflow.com/a/28130543/49630
        File file = File.createTempFile("fakeMultiPartRequest", "txt");
        FileUtils.write(file, "test data", "utf-8");
        FilePart<TemporaryFile> part = new MultipartFormData.FilePart<>(
                "key", "testfile.txt",
                Scala.Option("text/plain"), new TemporaryFile(file));
        List<FilePart<TemporaryFile>> fileParts = new ArrayList<>();
        fileParts.add(part);
        scala.collection.immutable.List<FilePart<TemporaryFile>> files = scala.collection.JavaConversions
                .asScalaBuffer(fileParts).toList();
        MultipartFormData<TemporaryFile> formData = new MultipartFormData<TemporaryFile>(
                null, files, null, null);
        AnyContent anyContent = new AnyContentAsMultipartFormData(formData);

        Result result = callAction(
                controllers.routes.ref.Application.index(),
                fakeRequest().withAnyContent(anyContent,
                        "multipart/form-data", "POST"));

        // Your Tests
        assertThat(status(result)).isEqualTo(OK);
        assertThat(contentAsString(result)).isEqualTo("test data");
    }

}
