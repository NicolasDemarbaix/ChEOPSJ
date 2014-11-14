package be.ac.ua.ansymo.cheopsj.visualizer.views.graph;

import java.io.ObjectInputStream.GetField;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.zest.core.viewers.IConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IFigureProvider;

import be.ac.ua.ansymo.cheopsj.model.famix.FamixEntity;
import be.ac.ua.ansymo.cheopsj.model.famix.FamixPackage;
import be.ac.ua.ansymo.cheopsj.visualizer.views.graph.figures.FPackageFigure;

public class ChangeGraphLabelProvider extends LabelProvider implements IConnectionStyleProvider, IFigureProvider {

	/*
	 * ================================
	 * LabelProvider Methods
	 * ================================
	 */
	@Override
	public Image getImage(Object element) {
		/*if (element instanceof FamixEntity) {
			return constructFamixImage((FamixEntity) element);
		}*/
		return null;
	}
	
	private Image constructFamixImage(FamixEntity ent) {
		if (ent.getNumOfTotalChanges() == 0) {
			return ent.getIcon();
		} else {
			Image icon = ent.getIcon();
			int iwidth = icon.getBounds().width;
			int iheight = icon.getBounds().height;
		  
			double sizeH = getSizeHeuristic(ent.getMostRecentTimeStamp());
			int imWidth = (int) (iwidth*sizeH);
			int imHeight = (int) (iheight*sizeH);
			Image img = new Image(null, imWidth,imHeight);
			GC gc = new GC(img);
			gc.setBackground(new org.eclipse.swt.graphics.Color(null, 0, 255, 0));
			int addAngle = 360*(ent.getNumOfAddedChanges()/ent.getNumOfTotalChanges());
		  	gc.fillArc(0, 0, imWidth, imHeight, 0, 50);
		  	gc.setBackground(new org.eclipse.swt.graphics.Color(null, 255, 255, 0));
		  	int modAngle = 360*(ent.getNumOfModifyChanges()/ent.getNumOfTotalChanges());
		  	gc.fillArc(0, 0, imWidth, imHeight, addAngle, modAngle);
		  	gc.setBackground(new org.eclipse.swt.graphics.Color(null, 255, 0, 0));
		  	int remAngle = 360*(ent.getNumOfRemovedChanges()/ent.getNumOfTotalChanges());
		  	gc.fillArc(0, 0, imWidth, imHeight, addAngle+modAngle, remAngle);
		  
		  	int xcoord = (imWidth/2)-(iwidth/2);
		  	int ycoord = (imHeight/2)-(iheight/2);
		  	gc.drawImage(icon, xcoord, ycoord);
		  
		  	return img;
		}
	}
	
	static public Timestamp now() {
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
		return currentTimestamp;
	}
	
	private double getSizeHeuristic(Date d) {
		long HOUR = 3600000;
		long DAY = 86400000;
		long WEEK = 604800000;
		
		long age = now().getTime() - d.getTime();
		
		if (age < HOUR) {
			return 3.0;
		} else if (age < DAY) {
			return 2.5;
		} else if (age < WEEK) {
			return 2.0;
		} else {
			return 1.5;
		}
	}
	
	@Override
	public String getText(Object element) {
		/*if (element instanceof FamixEntity) {
			return ((FamixEntity) element).getUniqueName();
		}
		return "node";*/
		return "";
	}
	
	/*
	 * ================================
	 * IConnectionStyleProvider Methods
	 * ================================
	 */
	@Override
	public int getConnectionStyle(Object rel) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Color getColor(Object rel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getHighlightColor(Object rel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLineWidth(Object rel) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IFigure getTooltip(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/*
	 * ================================
	 * IFigureProvider Methods
	 * ================================
	 */
	@Override
	public IFigure getFigure(Object element) {
		if (element instanceof FamixEntity) {
			Image img = constructFamixImage((FamixEntity) element);
			Figure result = null;
			if (element instanceof FamixPackage) {
				result = new FPackageFigure(img, ((FamixEntity) element).getUniqueName());
			}
			if (result != null) 
				result.setSize(-1, -1);
		}
		return null;
	}

}
