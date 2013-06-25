import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author DeadLox
 *
 */
public class Flashiz {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BufferedReader r = new BufferedReader(new FileReader("Flashiz.txt"));
			
			String line = new String();
			int lineNum = 0;
			
			int nbParticipant = 0;
			List<int[]> listeParticpant = new ArrayList<int[]>();
			int nbQRCode = 0;
			List<Float[]> listeQRCode = new ArrayList<Float[]>();
			
			// On lit le fichier
			while ((line = r.readLine()) != null) {
				// Récupère le nombre de participant
				if (lineNum == 0) {
					nbParticipant = Integer.parseInt(line);
				// Récupère la liste des participants
				} else if (lineNum <= nbParticipant) {
					System.out.println(line);
					String[] participant = line.split(";");
					int vitesse = Integer.parseInt(participant[1].replaceAll(";", ""));
					int capacite = Integer.parseInt(participant[2].replaceAll(";", ""));
					listeParticpant.add(new int[]{Integer.parseInt(participant[1]), Integer.parseInt(participant[2])});
				// Récupère le nombre de QRCodes
				} else if (lineNum == nbParticipant+1) {
					nbQRCode = Integer.parseInt(line);
				// Récupère la liste des QRCodes
				} else if (lineNum > nbParticipant && lineNum <= nbParticipant+nbQRCode+1) {
					String[] QRCode = line.split(";");
					Float distance = Float.parseFloat(QRCode[0]);
					Float difficulte = Float.parseFloat(QRCode[1]);
					listeQRCode.add(new Float[]{distance, difficulte});
				}
				lineNum++;
			}
			
			// Récupère la liste des possibilités
			List<int[]> listePossibilite = getPossibilite(listeParticpant);
			System.out.println(listePossibilite.size());
			
			// Calcul les performances de chaques équipes
			List<Double> listePerf = calcPerf(listeParticpant, listePossibilite, listeQRCode);
			System.out.println(listePerf);
			
			dump(listePossibilite, listePerf);
			
			int indexBestPerf = getBestPerf(listePerf);
			showEquipe(listePossibilite.get(indexBestPerf));
			System.out.println("Meilleur performances: "+listePerf.get(indexBestPerf));
			
			System.out.println(listeParticpant);
			System.out.println(listeQRCode);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void showEquipe(int[] listeParticipants){
		System.out.println("Meilleur équipe: "+listeParticipants[0]+" "+listeParticipants[1]+" "+listeParticipants[2]);
	}
	
	public static void dump(List<int[]> listPossibilite, List<Double> listePerf){
		int nb = 0;
		for (int[] possibilite : listPossibilite) {
			System.out.println("["+nb+"] "+possibilite[0]+" "+possibilite[1]+" "+possibilite[2]+" = "+listePerf.get(nb));
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
		} else if (p3[0] < vitesseMin) {
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
	public static List<Double> calcPerf(List<int[]> listeParticpant, List<int[]> listPossibilite, List<Float[]> listeQRCode){
		List<Double> listePerf = new ArrayList<Double>();
		if (listPossibilite.size() > 0) {
			for (int[] possibilite : listPossibilite) {
				int indexP1 = possibilite[0];
				int indexP2 = possibilite[1];
				int indexP3 = possibilite[2];
				int vitesseMinEquipe = getVitesseMin(listeParticpant.get(indexP1), listeParticpant.get(indexP2), listeParticpant.get(indexP3));
				int capaciteEquipe = getCapacite(listeParticpant.get(indexP1), listeParticpant.get(indexP2), listeParticpant.get(indexP3));
				float temps = 0;
				for (Float[] QRCode : listeQRCode) {
					float tps = (QRCode[0] / vitesseMinEquipe) + (QRCode[1] / capaciteEquipe);
					temps += tps;
				}
				listePerf.add(Math.round(temps*100.0) / 100.0);
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
				for (int p2=0; p2 < listeParticpant.size(); p2++) {
					if (p2 > p1) {
						for (int p3=0; p3 < listeParticpant.size(); p3++) {
							if (p3 > p2) {
								int[] possibilite = new int[]{p1, p2, p3};
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
