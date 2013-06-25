package flashiz;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * 
 * @author DeadLox
 *
 */
public class Flashiz {
	public static DecimalFormat df = new DecimalFormat("#0.##");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BufferedReader r = new BufferedReader(new FileReader("src/flashiz/Flashiz3.txt"));
			
			String line = new String();
			int lineNum = 0;
			
			int nbParticipant = 0;
			List<int[]> listeParticpant = new ArrayList<int[]>();
			int nbQRCode = 0;
			List<Double[]> listeQRCode = new ArrayList<Double[]>();
			
			// On lit le fichier
			while ((line = r.readLine()) != null) {
				// Récupère le nombre de participant
				if (lineNum == 0) {
					nbParticipant = Integer.parseInt(line);
					for (int i=0; i<nbParticipant; i++) {
						listeParticpant.add(new int[]{});
					}
				// Récupère la liste des participants
				} else if (lineNum <= nbParticipant) {
					String[] participant = line.split(";");
					int index = Integer.parseInt(participant[0])-1;
					int vitesse = Integer.parseInt(participant[1]);
					int capacite = Integer.parseInt(participant[2]);
					listeParticpant.set(index, new int[]{Integer.parseInt(participant[1]), Integer.parseInt(participant[2])});
				// Récupère le nombre de QRCodes
				} else if (lineNum == nbParticipant+1) {
					nbQRCode = Integer.parseInt(line);
				// Récupère la liste des QRCodes
				} else if (lineNum > nbParticipant && lineNum <= nbParticipant+nbQRCode+1) {
					String[] QRCode = line.split(";");
					double distance = Double.parseDouble(QRCode[0]);
					double difficulte = Double.parseDouble(QRCode[1]);
					listeQRCode.add(new Double[]{distance, difficulte});
				}
				lineNum++;
			}
			//dumpList(listeParticpant);
			
			// Récupère la liste des possibilités
			List<int[]> listePossibilite = getPossibilite(listeParticpant);
			
			// Calcul les performances de chaques équipes
			List<Double> listePerf = calcPerf(listeParticpant, listePossibilite, listeQRCode);
			
			//dump(listePossibilite, listePerf);
			
			int indexBestPerf = getBestPerf(listePerf);
			;
			System.out.println(showEquipe(listePossibilite.get(indexBestPerf))+";"+listePerf.get(indexBestPerf));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String showEquipe(int[] listeParticipants){
		Arrays.sort(listeParticipants);
		return (listeParticipants[0]+1)+";"+(listeParticipants[1]+1)+";"+(listeParticipants[2]+1);
	}
	
	public static void dumpList(List<int[]> listeParticpant){
		int nb = 0;
		for (int[] participant : listeParticpant) {
			System.out.println("[Equipe n°"+(nb+1)+"] "+participant[0]+" "+participant[1]);
			nb++;
		}
	}
	
	public static void dump(List<int[]> listPossibilite, List<Double> listePerf){
		int nb = 0;
		for (int[] possibilite : listPossibilite) {
			System.out.println("["+nb+"] "+(possibilite[0]+1)+" "+(possibilite[1]+1)+" "+(possibilite[2]+1)+" = "+listePerf.get(nb));
			nb++;
		}
	}
	
	/**
	 * Récupère la vitesse minimum d'une équipe
	 * 
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	public static int getVitesseMin(int[] p1, int[] p2, int[] p3){
		int vitesseMin = p1[0];
		if (p2[0] < vitesseMin) {
			vitesseMin = p2[0];
		}
		if (p3[0] < vitesseMin) {
			vitesseMin = p3[0];
		}
		return vitesseMin;
	}
	
	/**
	 * Récupère la capacité d'une équipe
	 * 
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	public static int getCapacite(int[] p1, int[] p2, int[] p3){
		int capacite = p1[1]+p2[1]+p3[1];
		return capacite;
	}
	
	/**
	 * Récupère les meilleures performances dans la liste
	 * 
	 * @param listePerf
	 * @return
	 */
	public static int getBestPerf(List<Double> listePerf){
		int indexBestPerf = 0;
		double bestPerf = listePerf.get(0);
		for (int i=0; i<listePerf.size(); i++) {
			double perf = listePerf.get(i);
			if (perf < bestPerf) {
				bestPerf = perf;
				indexBestPerf = i;
			}
		}
		return indexBestPerf;
	}
	
	/**
	 * Calcul les performances d'une équipe
	 */
	public static List<Double> calcPerf(List<int[]> listeParticpant, List<int[]> listPossibilite, List<Double[]> listeQRCode){
		List<Double> listePerf = new ArrayList<Double>();
		if (listPossibilite.size() > 0) {
			for (int[] possibilite : listPossibilite) {
				// On récupère les trois participants de l'équipe
				int indexP1 = possibilite[0];
				int indexP2 = possibilite[1];
				int indexP3 = possibilite[2];
				// Calcul de la vitesse de l'équipe
				int vitesseMinEquipe = getVitesseMin(listeParticpant.get(indexP1), listeParticpant.get(indexP2), listeParticpant.get(indexP3));
				// Calcul de la capacité de l'équipe
				int capaciteEquipe = getCapacite(listeParticpant.get(indexP1), listeParticpant.get(indexP2), listeParticpant.get(indexP3));
				double temps = 0;
				// On boucle sur tous les QRCodes
				for (Double[] QRCode : listeQRCode) {
					double distance = QRCode[0];
					double difficulte = QRCode[1];
					// Calcul du temps pour trouver le QRCode
					double tps = (distance / vitesseMinEquipe) + (difficulte / capaciteEquipe);
					// On arrondit à deux décimales
					tps = Double.parseDouble(df.format(tps).replace(",", "."));
					temps += tps;
				}
				// On arrondit à deux décimales
				temps = Double.parseDouble(df.format(temps).replace(",", "."));
				listePerf.add(temps);
			}
		}
		return listePerf;
	}
	
	/**
	 * Méthode permettant de retouner un tableau de toutes les possibilités d'éauipes
	 */
	public static List<int[]> getPossibilite(List<int[]> listeParticpant){
		List<int[]> listePossibilite = new ArrayList<int[]>();
		if (listeParticpant.size() > 0) {
			for (int p1=0; p1 < listeParticpant.size(); p1++) {
				for (int p2=p1; p2 < listeParticpant.size(); p2++) {
					if (p1 != p2) {
						for (int p3=p2; p3 < listeParticpant.size(); p3++) {
							if (p2 != p3 && p1 != p3) {
								int[] possibilite = new int[]{p1, p2, p3};
								Arrays.sort(possibilite);
								if (!listePossibilite.contains(possibilite)) {
									listePossibilite.add(possibilite);
								}
							}
						}
					}
				}
			}
		}
		return listePossibilite;
	}

}
