package be.ac.ua.ansymo.cheopsj.changerecorders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import be.ac.ua.ansymo.cheopsj.model.ModelManager;
import be.ac.ua.ansymo.cheopsj.model.ModelManagerChange;
import be.ac.ua.ansymo.cheopsj.model.changes.Add;
import be.ac.ua.ansymo.cheopsj.model.changes.AtomicChange;
import be.ac.ua.ansymo.cheopsj.model.changes.Remove;
import be.ac.ua.ansymo.cheopsj.model.famix.FamixPackage;

public class PackageRemovalTest {

	private String p1 = "be";
	private String p2 = p1 + ".ac";
	private String p3 = p2 + ".ua";
	private String p4 = p3 + ".test";
	private String p5 = p4 + ".pack";
	private PackageRecorder recorder1;
	private ModelManager manager;
	private ModelManagerChange managerChange;

	@Before
	public void setUp() throws Exception {
		manager = ModelManager.getInstance();
		managerChange = ModelManagerChange.getInstance();
		PackageRecorder rec = new PackageRecorder(p5);
		rec.storeChange(new Add());
	}

	@After
	public void tearDown() throws Exception {
		manager.clearModel();
	}

	@Test
	public void test() {
		//When removing a package, the removal is structurally depends on the package's addition
		recorder1 = new PackageRecorder(p5);
		recorder1.storeChange(new Remove());
		assertEquals(6, managerChange.getChanges().size());

		FamixPackage pack = manager.getFamixPackage(p5);
		assertEquals(2,pack.getAffectingChanges().size());
		Remove rem = managerChange.getLatestRemoval(pack);
		AtomicChange add = managerChange.getLastestAddition(pack);

		assertTrue(rem.getStructuralDependencies().contains(add));
		assertTrue(add.getStructuralDependees().contains(rem));
	}

	@Test
	public void test2(){
		recorder1 = new PackageRecorder(p1);
		recorder1.storeChange(new Remove());
		assertEquals(10, managerChange.getChanges().size());

		//When removing the topmost package, all subpackages need also be removed
		removalDependsOnChildRemoval(p1,p2);
		removalDependsOnChildRemoval(p2,p3);
		removalDependsOnChildRemoval(p3,p4);
		removalDependsOnChildRemoval(p4,p5);

		FamixPackage pack = manager.getFamixPackage(p5);
		Remove rem = managerChange.getLatestRemoval(pack);
		assertEquals(1, rem.getStructuralDependencies().size());
	}

	private void removalDependsOnChildRemoval(String name, String childname) {
		FamixPackage pack = manager.getFamixPackage(name);
		Remove rem = managerChange.getLatestRemoval(pack);
		assertFalse(rem.getStructuralDependencies().isEmpty());
		FamixPackage pack2 = manager.getFamixPackage(childname);
		Remove rem2 = managerChange.getLatestRemoval(pack2);
		assertTrue(rem.getStructuralDependencies().contains(rem2));
		
		AtomicChange add = managerChange.getLastestAddition(pack);
		assertTrue(rem.getStructuralDependencies().contains(add));
	}
	
	@Test
	public void test3(){
		//First removing all children and then removing the package should also work.
		recorder1 = new PackageRecorder(p5);
	
		Remove childrem = new Remove();
		recorder1.storeChange(childrem);
		
		recorder1 = new PackageRecorder(p4);
		Remove rem = new Remove();
		recorder1.storeChange(rem);
		
		assertEquals(7, managerChange.getChanges().size());
		assertTrue(rem.getStructuralDependencies().contains(childrem));
	}

}
