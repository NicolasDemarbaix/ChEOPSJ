package be.ac.ua.ansymo.cheopsj.visualizer.views.graph.figures;

import java.awt.Color;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * FPackageFigure class
 * @author nicolasdemarbaix
 *
 * This class is used for drawing a famix package in the change graph. It provides 
 */
public class FPackageFigure extends Figure {
	private Image packageImg = null;
	private Label label = null;
	
	public FPackageFigure(Image img, String l) {
		this.packageImg = img;
		this.label = new Label(l);
		this.label.setLabelAlignment(Label.CENTER);
		
		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
		setOpaque(true);
		setBorder(new LineBorder(ColorConstants.black, 1));
		setBackgroundColor(ColorConstants.lightGray);
				
		ImageFigure imgFig = new ImageFigure(this.packageImg);
		add(imgFig);
		add(this.label);
		
	}
	
	public static void main(String[] args) {
		final Display display = new Display();
		
		Shell shell = new Shell(display);
		
		//==============================
		  Image icon = new Image(display, "/Users/nicolasdemarbaix/Documents/workspace_thesis/be.ac.ua.ansymo.cheopsj.visualizer/src/be/ac/ua/ansymo/cheopsj/visualizer/views/graph/figures/package_obj.gif");
		  int iwidth = icon.getBounds().width;
		  int iheight = icon.getBounds().height;
		  
		  double sizeH = 3.0;
		  int imWidth = (int) (iwidth*sizeH);
		  int imHeight = (int) (iheight*sizeH);
		  Image img = new Image(display, imWidth,imHeight);
		  //img.setBackground(new org.eclipse.swt.graphics.Color(display, 224,224,224));
		  GC gc = new GC(img);
		  gc.setBackground(new org.eclipse.swt.graphics.Color(display, 192,192,192));
		  gc.fillRectangle(0, 0, imWidth, imHeight);
		  gc.setBackground(new org.eclipse.swt.graphics.Color(display, 0, 255, 0));
		  gc.fillArc(0, 0, imWidth, imHeight, 0, 50);
		  gc.setBackground(new org.eclipse.swt.graphics.Color(display, 255, 255, 0));
		  gc.fillArc(0, 0, imWidth, imHeight, 50, 150);
		  gc.setBackground(new org.eclipse.swt.graphics.Color(display, 255, 0, 0));
		  gc.fillArc(0, 0, imWidth, imHeight, 200, 160);
		  
		  int xcoord = (imWidth/2)-(iwidth/2);
		  int ycoord = (imHeight/2)-(iheight/2);
		  gc.drawImage(icon, xcoord, ycoord);
		 //=================================
		
		LightweightSystem lws = new LightweightSystem(shell);
		String[] name = "package.example".split("\\.");
		Figure fpack = new FPackageFigure(img, name[name.length-1]);
		lws.setContents(fpack);
		shell.setSize(150,72);
		System.out.println("Figure size: " + fpack.getBounds());
		System.out.println("Shell size: " + shell.getSize());
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		display.dispose();
	}
}