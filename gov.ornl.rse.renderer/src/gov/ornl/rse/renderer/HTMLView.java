package gov.ornl.rse.renderer;

public class HTMLView {

	public HTMLView() {
		
	}
	
	public void draw(DataElement data) {
		String content = "<html><head><title>Hello World!</title></head><body>Hello World! " + 
						data.toString() + "</body></html>";
		System.out.println(content);
		return;
	}

}
