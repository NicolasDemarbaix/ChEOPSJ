package be.ac.ua.ansymo.cheopsj.visualizer.views.graph;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.zest.core.viewers.IConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IFigureProvider;

import be.ac.ua.ansymo.cheopsj.model.ModelManager;
import be.ac.ua.ansymo.cheopsj.model.ModelManagerChange;
import be.ac.ua.ansymo.cheopsj.model.famix.FamixAttribute;
import be.ac.ua.ansymo.cheopsj.model.famix.FamixClass;
import be.ac.ua.ansymo.cheopsj.model.famix.FamixEntity;
import be.ac.ua.ansymo.cheopsj.model.famix.FamixMethod;
import be.ac.ua.ansymo.cheopsj.model.famix.FamixPackage;
import be.ac.ua.ansymo.cheopsj.visualizer.views.graph.figures.FamixFigure;

public class ChangeGraphLabelProvider extends LabelProvider implements IConnectionStyleProvider, IFigureProvider {

	private ModelManagerChange changeManager = null;
	
	public ChangeGraphLabelProvider() {
		this.changeManager = ModelManagerChange.getInstance();
	}
	/*
	 * ================================
	 * LabelProvider Methods
	 * ================================
	 */
	
	@Override
	public String getText(Object element) {
		return "";
	}
	
	/**
	 * Aggregate the number of changes that occured inside a class
	 * @param c - (FamixClass) the class under consideration
	 * @return changes - (int[]) aggregated result (totalChanges, addChanges, deleteChanges, modificationChanges)
	 */
	private int[] getClassChanges(FamixClass c) {
		int[] changes = {0,0,0,0};
		
		try {
			Collection<FamixAttribute> attribute_col = c.getAttributes();
			for (FamixAttribute a : attribute_col) {
				changes[0] += this.changeManager.getChangeCount(a);
				changes[1] += this.changeManager.getAddCount(a);
				changes[2] += this.changeManager.getRemoveCount(a);
				//changes[3] += this.changeManager.getModificationCount(a);
			}
			
			Collection<FamixMethod> method_col = c.getMethods();
			for (FamixMethod m : method_col) {
				changes[0] += this.changeManager.getChangeCount(m);
				changes[1] += this.changeManager.getAddCount(m);
				changes[2] += this.changeManager.getRemoveCount(m);
				//changes[3] += this.changeManager.getModificationCount(m);
			}

			Collection<FamixClass> class_col = c.getNestedClasses();
			for (FamixClass cc : class_col) {
				changes[0] += this.changeManager.getChangeCount(cc);
				changes[1] += this.changeManager.getAddCount(cc);
				changes[2] += this.changeManager.getRemoveCount(cc);
				//changes[3] += this.changeManager.getModificationCount(cc);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return changes;
	}
	
	/**
	 * Aggregate the number of changes that occured inside a package
	 * @param pack - (FamixPackage) the package under consideration
	 * @return changes - (int[]) the aggregated result (totalChanges, addChanges, deleteChanges, modificationChanges)
	 */
	private int[] getPackageChanges(FamixPackage pack) {		
		int[] changes = {0,0,0,0};

		
		Collection<FamixClass> classes = pack.getClasses();
		
		for (FamixClass c : classes) {
			changes[0] += this.changeManager.getChangeCount(c);
			changes[1] += this.changeManager.getAddCount(c);
			changes[2] += this.changeManager.getRemoveCount(c);
			//changes[3] += this.changeManager.getModificationCount(c);
			
			try {
				Collection<FamixAttribute> attribute_col = c.getAttributes();
				for (FamixAttribute a : attribute_col) {
					changes[0] += this.changeManager.getChangeCount(a);
					changes[1] += this.changeManager.getAddCount(a);
					changes[2] += this.changeManager.getRemoveCount(a);
			//		changes[3] += this.changeManager.getModificationCount(a);
				}
				
				Collection<FamixMethod> method_col = c.getMethods();
				for (FamixMethod m : method_col) {
					changes[0] += this.changeManager.getChangeCount(m);
					changes[1] += this.changeManager.getAddCount(m);
					changes[2] += this.changeManager.getRemoveCount(m);
			//		changes[3] += this.changeManager.getModificationCount(m);
				}

				Collection<FamixClass> class_col = c.getNestedClasses();
				for (FamixClass cc : class_col) {
					changes[0] += this.changeManager.getChangeCount(cc);
					changes[1] += this.changeManager.getAddCount(cc);
					changes[2] += this.changeManager.getRemoveCount(cc);
			//		changes[3] += this.changeManager.getModificationCount(cc);
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		return changes;
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
		System.out.println("CHANGEGRAPHLABELPROVIDER::GETFIGURE::ACCESSED");
		if (element instanceof FamixEntity) {
			int[] changes = {0,0,0,0};
			
			if (element instanceof FamixPackage) {
				changes = getPackageChanges((FamixPackage) element);
			} else if (element instanceof FamixClass) {
				changes = getClassChanges((FamixClass) element);
			}
			
			Date lchange = this.changeManager.getLatestChange((FamixEntity)element).getTimeStamp();
			System.out.println("CHANGEGRAPHLABELPROVIDER::GETFIGURE::BUILDING FIGURE");
			Figure fig = new FamixFigure(changes, (FamixEntity)element, lchange);
			fig.setSize(-1, -1);
			System.out.println("CHANGEGRAPHLABELPROVIDER::GETFIGURE:: FIGURE BUILT");
			System.out.println("RETURNING FIGURE FOR ENTITY: " + ((FamixEntity)element).getUniqueName());
			return fig;
		}
		return null;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * ================================
	 * ChangeGraphLabelProvider Methods
	 * ================================
	 */
	public void showInfoDialog(String element) {
		if (ModelManager.getInstance().famixPackageExists(element)) {
			FamixPackage pack = ModelManager.getInstance().getFamixPackage(element);
			MessageBox dialog = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_INFORMATION | SWT.OK);
			dialog.setText("Package Information: " + element);
			dialog.setMessage(constructDialogString(pack));
			dialog.open();
		} else {
			MessageBox dialog = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.open();
		}
	}
	
	private String constructDialogString(FamixPackage pack) {
		String dialogString = "";
		
		int[] changes = getPackageChanges(pack);
		
		dialogString = "This package contains: \n" + 
				"   * Information about " + pack.getClasses().size() + " classes (some of which might have been deleted)\n" + 
			//	"   * " + pack.getGlobalVariable().size() + " global variables\n" + 
			//	"   * " + pack.getPackages() + " sub packages\n" +
				"\n Information about changes: \n" +
				"   * " + pack.getAffectingChanges().size() + " changes affect this package\n" + 
				"   * " + changes[0] + " changes occured to elements in this package\n" + 
				"   * " + changes[1] + " of these changes were additions\n" + 
				"   * " + changes[2] + " of these changes were removals\n" /*+ 
				"   * " + changes[3] + " of these changes were modifications\n"*/;
		
		return dialogString;
	}
}
