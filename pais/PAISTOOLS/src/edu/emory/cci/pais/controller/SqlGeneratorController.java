package edu.emory.cci.pais.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JCheckBox;

import edu.emory.cci.pais.dataloader.db2helper.PAISDBHelper;
import edu.emory.cci.pais.util.TengUtils;
import edu.emory.cci.pais.view.SqlGeneratorPanel;

public class SqlGeneratorController {
	SqlGeneratorPanel generatorPanel;
	String basesqlfile = "create_tables_base.sql";
	String baseIndexfile = "index.sql";
	String basekeyfile = "keys.sql";

	public SqlGeneratorController(SqlGeneratorPanel generatorPanel)
	{
		this.generatorPanel=generatorPanel;
		
	}
	
	
	public boolean generateCreateSQL(PAISDBHelper db,String outputDir)
	{
		if(new File(outputDir).isDirectory()==false)
		{
			System.out.println("invalid output directory!");
			return false;
		}
		
		HashMap<String,String> templates= new HashMap<String,String>();
		
		for(JCheckBox j:this.generatorPanel.getTemplateList())
		{
			if(j.isSelected())
				templates.put(j.getLabel().toUpperCase(), "");
		}
		String tablefile = outputDir+File.separator+"create_table.sql";
		String indexfile = outputDir+File.separator+"index.sql";
		String keyfile = outputDir+File.separator+"key.sql";
		String combofile = outputDir+File.separator+"comboSQL.sql";

		StringBuffer dropsb = new StringBuffer();
		StringBuffer createsb = new StringBuffer();
		StringBuffer featuresb = new StringBuffer();
		StringBuffer insertTemplatesb = new StringBuffer();
		StringBuffer insertFeaturesb = new StringBuffer();
		StringBuffer insertTFRLsb = new StringBuffer();
		
		
		StringBuffer newIndexsb = new StringBuffer();
		StringBuffer newKeysb = new StringBuffer();
		
		String baseSql = TengUtils.readFileWithReturn(basesqlfile);
		String baseIndex = TengUtils.readFileWithReturn(baseIndexfile);
		String baseKey = TengUtils.readFileWithReturn(basekeyfile);
		PreparedStatement pstmt = db.getPreparedStatement("select templatename,description from pais.template");
		try {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()&&templates.containsKey(rs.getString(1).toUpperCase()))
			{
				dropsb.append(TengUtils.DROPTABLESQL.replaceFirst("tablename", rs.getString(1).toUpperCase()));
				createsb.append(TengUtils.MARKUPTABLESQL.replaceFirst("tablename", rs.getString(1).toUpperCase()));
				insertTemplatesb.append(TengUtils.INSERTTEMPLATESQL.replaceAll("templatenamevalue", rs.getString(1)).replaceAll("descriptionvalue", rs.getString(2)));
			    newKeysb.append(TengUtils.KEYSQL.replaceFirst("templatename", rs.getString(1).toUpperCase()));
			    newIndexsb.append(TengUtils.INDEXSQL.replaceAll("templatename", rs.getString(1)));
			}
		} catch (SQLException e) {
			return false;
		}
		PreparedStatement pstmt1 = db.getPreparedStatement("select featurename,type from pais.features");
		try {
			ResultSet rs = pstmt1.executeQuery();
			while(rs.next())
			{
				featuresb.append(",\n"+rs.getString(1)+" "+rs.getString(2));
				insertFeaturesb.append(TengUtils.INSERTFEATURESQL.replaceAll("featurenamevalue", rs.getString(1)).replaceAll("typevalue", rs.getString(2)));
			}
		} catch (SQLException e) {
			return false;
		}
		
		PreparedStatement pstmt2 = db.getPreparedStatement("select templatename,featurename from pais.templatefeaturesrl");
		try {
				ResultSet rs = pstmt2.executeQuery();
				while(rs.next()&&templates.containsKey(rs.getString(1).toUpperCase()))
				{
					insertTFRLsb.append(TengUtils.INSERTTFRLSQL.replaceAll("templatenamevalue", rs.getString(1)).replaceAll("featurenamevalue", rs.getString(2)));
				}
			} catch (SQLException e) {
				return false;
		}
		String sql = baseSql.replaceFirst(TengUtils.DROPTABLELABEL, dropsb.toString()).replaceFirst(TengUtils.MARKUPTABLELABEL, createsb.toString()).replaceFirst("--FEATUREREPLACEMENT", featuresb.toString()).replaceAll(TengUtils.INSERTTEMPLATELABEL, insertTemplatesb.toString()).replaceAll(TengUtils.INSERTFEATURELABEL, insertFeaturesb.toString()).replaceAll(TengUtils.INSERTTFRLLABEL, insertTFRLsb.toString());
		String index = baseIndex.replaceAll(TengUtils.NEWINDEXLABEL, newIndexsb.toString());
		String key = baseKey.replaceAll(TengUtils.NEWKEYLABEL, newKeysb.toString());
		
		if(this.generatorPanel.getComboRB().isSelected())//write sql into combo files
		{
			if(this.generatorPanel.getTableCB().isSelected()==false)
				sql="";
			if(this.generatorPanel.getKeyCB().isSelected()==false)
				key="";
			if(this.generatorPanel.getIndexCB().isSelected()==false)
				index="";
			try {
				System.out.println("writing combo sql into file "+combofile);
				FileWriter fw= new FileWriter(combofile);
				fw.write(sql+key+index);
				fw.close();
				System.out.println("done!");
			} catch (IOException e) {
				return false;
			}
			return false;
		}
		else//write sql into seperate files
		{
		if(this.generatorPanel.getTableCB().isSelected())//write into file contains sql to create tables
			{
			try {
				System.out.println("writing create table sql into file "+tablefile);
				FileWriter fw= new FileWriter(tablefile);
				fw.write(sql);
				fw.close();
				System.out.println("done!");
			} catch (IOException e) {
				return false;
			}
		}
		
		if(this.generatorPanel.getIndexCB().isSelected())//write index
		{
			try {
				System.out.println("writing create index sql into file "+indexfile);
				FileWriter fw= new FileWriter(indexfile);
				fw.write(index);
				fw.close();
				System.out.println("done!");
			} catch (IOException e) {
				return false;
			}
		}
		if(this.generatorPanel.getKeyCB().isSelected())//write keys
		{
			try {
				System.out.println("writing create key sql into file "+keyfile);
				FileWriter fw= new FileWriter(keyfile);
				fw.write(key);
				fw.close();
				System.out.println("done!");
			} catch (IOException e) {
				return false;
			}
		}	
		}//end if
		
		return true;
	}
	
	
	
	public static void main(String [] args)
	{
		PAISDBHelper db = new PAISDBHelper("localhost","50000","teng","terry08161043","tengdb");
	    new SqlGeneratorPanel(db);
	}
}
