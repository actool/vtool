package performance_evaluation;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Key;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

public class RunEverest {
	public static void main(String[] args) throws Exception {
		String root_img = "C:\\Users\\camil\\Desktop\\everet-img\\";
		String pathAutSpec = "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\+1000\\iut1000states.aut";
		String pathAutIUT = "C:\\Users\\camil\\Desktop\\Nova pasta (2)\\+1000\\iut1000states.aut";
		String everestJar = "C:\\Users\\camil\\Desktop\\everest.bat";
		
		Desktop d = Desktop.getDesktop();
		d.open(new File(everestJar));

		Thread.sleep(1000);
		
		
		Screen s = new Screen();
		s.type(root_img + "inp-model.PNG", pathAutSpec);
		s.type(Key.ENTER);
		
		
		s.type(root_img + "inp-iut.PNG", pathAutIUT);
		s.type(Key.ENTER);
		
		s.click(root_img + "cb-modelType.PNG");
		s.type(Key.DOWN);
		s.type(Key.ENTER);
		
		s.click(root_img + "cb-label.PNG");
		s.type(Key.DOWN);
		s.type(Key.ENTER);
		
		s.click(root_img + "item-menu-ioco.PNG");
		
		while (true) {
			try {
				System.currentTimeMillis();
				s.find(new Pattern(root_img + "btn-folder.PNG").similar(1.0f));				
			} catch (FindFailed e) {								
				break;
			}
		}
			
		s.click(root_img + "btn-verify.PNG");

	}

}
