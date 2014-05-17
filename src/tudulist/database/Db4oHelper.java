package tudulist.database;

import java.io.IOException;
import java.util.GregorianCalendar;

import tudulist.models.Task;
import android.content.Context;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;

public class Db4oHelper {
	private static ObjectContainer oc = null;
	private Context context;
	
	public Db4oHelper(Context con){
		this.context = con;
	}
	
	/*
	 * Create, open and close the Database
	 * */
	public ObjectContainer db(){
		try {
			if(oc == null || oc.ext().isClosed()){
				oc = Db4oEmbedded.openFile(dbConfig(), this.db4oDBFullPath(context));
				//now load the inicial data in the databas
				//loading data...
			}
			
			return oc;
			
		} catch (Exception e) {
			Log.e(Db4oHelper.class.getName(), e.getMessage());
			return null;
		}
	}
	
	/*
	 * Configure the behavior of the Database 
	 * */
	private EmbeddedConfiguration dbConfig() throws IOException{
		EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
		configuration.common().objectClass(Task.class).objectField("description").indexed(true);
		configuration.common().objectClass(Task.class).cascadeOnUpdate(true);
		configuration.common().objectClass(Task.class).cascadeOnActivate(true);
		//configuration.common().add(new UniqueFieldValueConstraint(Task.class, "id"));
		//para utilizar a classe GregorianCalendar
		configuration.common().objectClass(GregorianCalendar.class).callConstructor(true);
		return configuration;
	}
	
	private String db4oDBFullPath(Context ctx){
		return ctx.getDir("data", 0) + "/" + "tasksDB.db4o";
	}
	
	public void close(){
		if(oc != null){
			oc.close();
		}
	}
}




















