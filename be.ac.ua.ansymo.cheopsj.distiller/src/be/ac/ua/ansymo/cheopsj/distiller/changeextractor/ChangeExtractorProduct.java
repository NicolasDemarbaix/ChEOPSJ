package be.ac.ua.ansymo.cheopsj.distiller.changeextractor;


import java.util.Date;
import java.util.List;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeEntity;
import be.ac.ua.ansymo.cheopsj.changerecorders.ClassRecorder;
import be.ac.ua.ansymo.cheopsj.changerecorders.MethodRecorder;
import be.ac.ua.ansymo.cheopsj.changerecorders.FieldRecorder;
import be.ac.ua.ansymo.cheopsj.model.changes.AtomicChange;
import be.ac.ua.ansymo.cheopsj.model.changes.Add;
import be.ac.ua.ansymo.cheopsj.model.changes.Remove;


public class ChangeExtractorProduct {
	
	private String changeIntent;
	private Date changeDate;
	private String changeUser;

	public String getChangeIntent() {
		return changeIntent;
	}

	public void setChangeIntent(String changeIntent) {
		this.changeIntent = changeIntent;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public String getChangeUser() {
		return changeUser;
	}

	public void setChangeUser(String changeUser) {
		this.changeUser = changeUser;
	}

	public void convertChanges(List<SourceCodeChange> sourceCodeChanges) {
		for (SourceCodeChange scc : sourceCodeChanges) {
			SourceCodeEntity entity = scc.getChangedEntity();
			switch (scc.getChangeType()) {
			case ADDITIONAL_CLASS:
				if (entity.getType().isClass()) {
					ClassRecorder recorder = new ClassRecorder(entity,
							scc.getParentEntity());
					recorder.storeChange(createAddition());
				}
				break;
			case REMOVED_CLASS:
				if (entity.getType().isClass()) {
					ClassRecorder recorder = new ClassRecorder(entity,
							scc.getParentEntity());
					recorder.storeChange(createRemoval());
				}
				break;
			case ADDITIONAL_FUNCTIONALITY:
				if (entity.getType().isMethod()) {
					MethodRecorder recorder = new MethodRecorder(entity,
							scc.getParentEntity());
					recorder.storeChange(createAddition());
				}
				break;
			case REMOVED_FUNCTIONALITY:
				if (entity.getType().isMethod()) {
					MethodRecorder recorder = new MethodRecorder(entity,
							scc.getParentEntity());
					recorder.storeChange(createRemoval());
				}
				break;
			case ADDITIONAL_OBJECT_STATE:
				if (entity.getType().isField()) {
					FieldRecorder recorder = new FieldRecorder(entity,
							scc.getParentEntity());
					recorder.storeChange(createAddition());
				}
				break;
			case REMOVED_OBJECT_STATE:
				if (entity.getType().isField()) {
					FieldRecorder recorder = new FieldRecorder(entity,
							scc.getParentEntity());
					recorder.storeChange(createRemoval());
				}
				break;
			default:
				break;
			}
		}
	}

	public AtomicChange createAddition() {
		AtomicChange add = new Add();
		add.setIntent(changeIntent);
		add.setTimeStamp(utilDateToSqlTimestamp(changeDate));
		add.setUser(changeUser);
		return add;
	}

	public Remove createRemoval() {
		Remove rem = new Remove();
		rem.setIntent(changeIntent);
		rem.setTimeStamp(utilDateToSqlTimestamp(changeDate));
		rem.setUser(changeUser);
		return rem;
	}	

	private final java.sql.Timestamp utilDateToSqlTimestamp(java.util.Date utilDate) {
		return new java.sql.Timestamp(utilDate.getTime());
	}	
}