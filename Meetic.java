package kskills;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Meetic {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BufferedReader r = new BufferedReader(new FileReader("Meetic.txt"));
			
			String line = new String();
			int lineNum = 0;
			
			String num1 = null;
			
			// On lit le fichier
			while ((line = r.readLine()) != null) {
				if (lineNum == 0) {
					num1 = line;
				}
			}
			// Découpe le nombre
			Integer[] numTab = cutNum(num1);
			// Génère la liste des combinaisons possibles
			System.out.println(initListCombi(num1.length()));
			List<String> listCombiIndex = generateAllCombinaisons(initListCombi(num1.length()), num1);
			
			dumpList(listCombiIndex);
			List<String> listCombi = generateListCombi(listCombiIndex, numTab);
			dumpList(listCombi);
			
			System.out.println(findBestNum(listCombi, Integer.parseInt(num1)));
			r.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void dumpList(List<String> list){
		System.out.println("");
		System.out.println("Dump:");
		for (String string : list) {
			System.out.println(string);
		}
	}
	
	public static String findBestNum(List<String> listCombiIndex, int num1){
		int bestNum = 0;
		int ecartMin = -num1;
		System.out.println("");
		System.out.println(num1);
		System.out.println("");
		for (String combi : listCombiIndex) {
			int combiNum = Integer.parseInt(combi);
			int ecart = num1-combiNum;
			System.out.println(num1+" "+combiNum+" "+ecart);
			if (ecart < 0 && combiNum != num1 && ecart > ecartMin) {
				ecartMin = ecart;
				bestNum = combiNum;
			}
			
		}
		return ""+bestNum;
	}
	
	/**
	 * Génère la liste des combinaisons de nombres possible à partir de la liste des combinaisons d'index
	 * 
	 * @param listCombiIndex
	 * @param numTab
	 * @return
	 */
	public static List<String> generateListCombi(List<String> listCombiIndex, Integer[] numTab){
		List<String> listeCombi = new ArrayList<String>();
		for (String CombiIndex : listCombiIndex) {
			char[] combiArray = CombiIndex.toCharArray();
			String combi = "";
			for (char c : combiArray) {
				int index = Integer.parseInt(""+c);
				combi += numTab[index];
			}
			listeCombi.add(combi);
		}
		return listeCombi;
	}
	
	/**
	 * initialise la liste des combinaisons
	 * 
	 * @param length
	 * @return
	 */
	public static List<String> initListCombi(int length){
		List<String> listCombi = new ArrayList<String>();
		for (int i=0; i< length; i++) {
			listCombi.add(""+i);
		}
		return listCombi;
	}
	
	/**
	 * Génère la suite de la combinaison
	 * 
	 * @param numPart
	 * @param length
	 * @return
	 */
	public static List<String> generateNext(String numPart, int length){
		List<String> combis = new ArrayList<String>();
		for (int i=0; i<length; i++) {
			if (!numPart.contains(""+i)) {
				String newCombi = numPart;
				newCombi += ""+i;
				combis.add(newCombi);
			}
		}
		return combis;
	}
	
	/**
	 * Génère la liste des combinaisons possibles
	 * 
	 * @param numTab
	 * @return
	 */
	public static List<String> generateAllCombinaisons(List<String> listCombi, String num1){
		List<String> listCombiTemp = new ArrayList<String>();
		if (listCombi.size() > 0) {
			for (int i=0; i<listCombi.size(); i++) {
				List<String> combis = generateNext(listCombi.get(i), num1.length()); 
				listCombiTemp.addAll(combis);
			}
		}
		listCombi.clear();
		listCombi.addAll(listCombiTemp);
		if (listCombi.iterator().next().length() < num1.length()) {
			generateAllCombinaisons(listCombi, num1);
		}
		return listCombi;
	}
	
	/**
	 * Retourne une liste contenant chque chiffre du nombre passé en paramètre
	 * 
	 * @param num
	 * @return
	 */
	public static Integer[] cutNum(String num){
		Integer[] numTab = new Integer[num.length()];
		for (int i=0; i<num.length(); i++) {
			numTab[i] = Integer.parseInt(num.substring(i, i+1));
		}
		return numTab;
	}

}
