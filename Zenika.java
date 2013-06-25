import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * 
 * @author cleborgne
 *
 */
public class Zenika {
	public static Logger logger = Logger.getLogger("Zenika");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileInputStream fis = null;
        BufferedReader reader = null;
        
        try {
        	fis = new FileInputStream("exemple2.txt");
            reader = new BufferedReader(new InputStreamReader(fis));
         
            String line = reader.readLine();
            int lineNum = 0;
            
            int nbMembreTeam = 0;
            int nbPlats = 0;
            List<Integer> listePlats = new ArrayList<Integer>();
            
            while(line != null){
                //System.out.println(line);
                if (lineNum == 0) {
                	// Récupère le nombre de membre dans l'équipe
                	nbMembreTeam = Integer.parseInt(line);
                } else if (lineNum == 1) {
                	// Récupère le nombre de plats
                	nbPlats = Integer.parseInt(line);
                } else {
                	if (nbPlats > 0) {
	                	// Récupère la liste des plats
	                	listePlats.add(Integer.parseInt(line));
	                	nbPlats--;
                	}
                }
                lineNum++;
                line = reader.readLine();
            }
            // Tri de la liste des plats par ordre décroissant
            Collections.sort(listePlats, Collections.reverseOrder());
            
            List<Integer> arrayMember = createMemberArray(nbMembreTeam);
            
            if (listePlats.size() > 0) {
	            for (int plat : listePlats) {
	            	int indexMember = getLightMember(arrayMember);
	            	arrayMember.set(indexMember, arrayMember.get(indexMember) + plat);
	            }
            }
            
            System.out.println(getMax(arrayMember));
            
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Retourne le délais le plsu long de la liste
	 * 
	 * @param arrayMember
	 * @return
	 */
	public static int getMax(List<Integer> arrayMember){
		int max = 0;
		if (arrayMember.size() > 0) {
			for(int value : arrayMember) {
				if (max < value) {
					max = value;
				}
			}
		}
		return max;
	}
	
	/**
	 * Créé la liste des membres
	 * 
	 * @param nbMember
	 * @return
	 */
	public static List<Integer> createMemberArray(int nbMember){
		List<Integer> arrayMember = new ArrayList<Integer>();
		if (nbMember > 0) {
			for (int i=0; i < nbMember; i++) {
				arrayMember.add(0);
			}
		}
		return arrayMember;
	}
	
	/**
	 * Permet de récupèrer l'index du membre ayant le moins mangé
	 * 
	 * @param arrayMember
	 * @return
	 */
	public static int getLightMember(List<Integer> arrayMember){
		int index = 0;
		int lessValue = 0;
		if (arrayMember.size() > 0) {
			int key = 0;
			for(int value : arrayMember) {
			  if (lessValue == 0) {
				  lessValue = value;
			  } else if (lessValue > value) {
				  lessValue = value;
				  index = key;
			  }
			  key++;
			}
		}
		return index;
	}
}
