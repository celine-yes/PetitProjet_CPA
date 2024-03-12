package algorithms;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import supportGUI.Circle;

public class DefaultTeam {
	
  public static Random random = new Random();

  
  public Circle calculCercleMin(ArrayList<Point> points) {
	  //return cercleMinNaif(new ArrayList<Point>(points));
	  return b_md(new ArrayList<Point>(points), new ArrayList<Point>());
  }
  
  
  
  private static Circle cercleMinNaif(ArrayList<Point> inputpoints){
      ArrayList<Point> points = new ArrayList<Point>(inputpoints);
      if (points.size()<1) {
    	  return null;
      }
      double centreX,centreY,cRayon;
      boolean pointsDedans;
      
      for (Point p: points){
          for (Point q: points){
        	  centreX = (p.x + q.x)/2;
        	  centreY = (p.y + q.y)/2;
        	  cRayon = ((p.x - q.x)*(p.x - q.x)+(p.y - q.y)*(p.y - q.y))/4;
              pointsDedans = true; 
              
              //vérifie si tous les points sont dans le cercle
              for (Point s: points) {
            	  double distCentreK = (s.x - centreX)*(s.x - centreX)+(s.y - centreY)*(s.y - centreY);
                  if ( distCentreK > cRayon){
                	  pointsDedans = false;
                      break;
                  }
              }
              
              if (pointsDedans) {
            	  return new Circle(new Point((int)centreX, (int)centreY), (int)Math.sqrt(cRayon));
              }
          }
      }
      
      double resX=0;
      double resY=0;
      double resRayon=Double.MAX_VALUE;
      
      for (int i=0; i<points.size(); i++){
          for (int j=i+1; j<points.size(); j++){
              for (int k=j+1; k<points.size(); k++){
            	  
                  Point p = points.get(i);
                  Point q = points.get(j);
                  Point r = points.get(k);
                  
                  //3 points colinéaires
                  if ((q.x - p.x) * (r.y - p.y) - (q.y - p.y) * (r.x - p.x) == 0) {
                	  continue;
                  }
                  
                  if ((p.y == q.y) || (p.y == r.y)) {
                      if (p.y == q.y){
                          p = points.get(k);
                          r = points.get(i);
                      } else {
                          p = points.get(j);
                          q = points.get(i);
                      }
                  }
                  //calcul des coordonnees du cercle circonscrit des trois points pqr
                  double centrepqX = (p.x + q.x)/2;
                  double centrepqY = (p.y + q.y)/2;
                  double centreprX = (p.x + r.x)/2;
                  double centreprY = (p.y + r.y)/2;
                  
                  //droites passant par centrepq et perpendiculaire à pq, pareil pour centrepr
                  double a1 = (q.x - p.x) / (double)(p.y - q.y);
                  double b1 = centrepqY - a1 * centrepqX;
                  double a2 = (r.x - p.x)/(double)(p.y - r.y);
                  double b2 = centreprY - a2*centreprX;
                  
                  //centre c calculé à partir du points d'intersection des deux droites
                  centreX = (b2 - b1)/(double)(a1 - a2);
                  centreY = a1*centreX + b1;
                  cRayon = (p.x - centreX)*(p.x - centreX)+(p.y - centreY)*(p.y - centreY);
                  
                  if (cRayon>=resRayon) {
                	  continue;
                  }
                  pointsDedans = true;
                  
                  //vérifie si tous les points sont dans le cercle
                  for (Point s: points) {
                	  double distCentreK = (s.x - centreX)*(s.x - centreX)+(s.y - centreY)*(s.y -  centreY);
                      if (distCentreK > cRayon){
                    	  pointsDedans = false;
                          break;
                      }
                  }
               
                  if (pointsDedans) {
                	  resX = centreX;
                	  resY = centreY;
                	  resRayon = cRayon;
                  }
              }
          }
      }
      return new Circle(new Point((int)resX, (int)resY), (int)Math.sqrt(resRayon));
  }
  
  
  
  
  private static Circle b_md(ArrayList<Point> points, ArrayList<Point> bordure) {
	  
	  Circle disk;
	  if (points.size()== 0 || bordure.size()==3) {
		  if (bordure.size() == 3) {
	            disk = cercle3Points(bordure.get(0), bordure.get(1), bordure.get(2));
	        } else {
	            disk = new Circle(new Point(0, 0), 0); //par défaut, si aucun points
	        }
	  }
	  else {
		  ArrayList<Point> tmpPoints = new ArrayList<Point>(points);
		  Point p = tmpPoints.get(random.nextInt(tmpPoints.size()));
		  tmpPoints.remove(p);
		  disk = b_md (tmpPoints, bordure);
		  if (disk != null) {
			  Point center = disk.getCenter();
			  double radius = disk.getRadius();
			  
			  //vérifie si p est à l'extérieur de disk
			  if (center.distance(p) > radius) {
				  ArrayList<Point> tmpBordure = new ArrayList<Point>(bordure);
				  tmpBordure.add(p);
				  disk = b_md(tmpPoints, tmpBordure);
			  }
		  } 
	  } 
	  return disk;
  }
  
  
  private static Circle cercle3Points(Point p1, Point p2, Point p3) {
	  //par optimisation, on devrait faire le calcul de distance à la main
      double distP1P2 = p1.distance(p2);
      double distP2P3 = p2.distance(p3);
      double distP3P1 = p3.distance(p1);

      //vérifie si les points sont alignés
      double determinant = p1.x * (p2.y - p3.y) + p2.x * (p3.y - p1.y) + p3.x * (p1.y - p2.y);
      if (Math.abs(determinant) < 1e-9) {
          //points alignés, retourne le cercle avec le centre au milieu du segment le plus long
          Point center;
          double radius;
          if (distP1P2 >= distP2P3 && distP1P2 >= distP3P1) {
              center = new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
              radius = distP1P2 / 2;
          } else if (distP2P3 >= distP1P2 && distP2P3 >= distP3P1) {
              center = new Point((p2.x + p3.x) / 2, (p2.y + p3.y) / 2);
              radius = distP2P3 / 2;
          } else {
              center = new Point((p3.x + p1.x) / 2, (p3.y + p1.y) / 2);
              radius = distP3P1 / 2;
          }
          return new Circle(center, (int)radius);
      }

      //calcul des coordonnées du centre du cercle
      double a = p2.x - p1.x;
      double b = p2.y - p1.y;
      double c = p3.x - p1.x;
      double d = p3.y - p1.y;
      double e = a * (p1.x + p2.x) + b * (p1.y + p2.y);
      double f = c * (p1.x + p3.x) + d * (p1.y + p3.y);
      double g = 2 * (a * (p3.y - p2.y) - b * (p3.x - p2.x));
      
      double centerX = (d * e - b * f) / g;
      double centerY = (a * f - c * e) / g;

      //calcul du rayon du cercle
      Point newPoint = new Point((int)centerX, (int)centerY);
      double radius = p1.distance(newPoint);

      return new Circle(newPoint, (int)radius);
  }
  
  
  
  /** ----------------fonctions pour lire et écrire dans les fichiers tests -------------------------**/
  
  
  
  public static ArrayList<Point> readPointsFromFile(String fileName) {
      ArrayList<Point> points = new ArrayList<>();
      try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
          String line;
          while ((line = br.readLine()) != null) {
              String[] parts = line.split(" ");
              int x = Integer.parseInt(parts[0]);
              int y = Integer.parseInt(parts[1]);
              points.add(new Point(x, y));
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
      return points;
  }
  
  public static void WriteCPUTimeBMDToFile(String filePath, String filename, String outputPath) {
	  ArrayList<Point> points = readPointsFromFile(filePath + filename);
      try (FileWriter writer = new FileWriter(outputPath, true)) {
          ArrayList<Point> bordure = new ArrayList<>();

          long startTime = System.nanoTime();
          b_md(points, bordure);
          long endTime = System.nanoTime();

          long duration = (endTime - startTime); //durée en nanosecondes
          double durationInMilliseconds = duration / 1e6; //convertit en millisecondes

          writer.write(filename + " " + durationInMilliseconds + " ms\n");
          System.out.println(durationInMilliseconds + " ms");
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
  
  
  public static void WriteCPUTimeNaifToFile(String filePath, String filename, String outputPath) {
	  ArrayList<Point> points = readPointsFromFile(filePath + filename);
      try (FileWriter writer = new FileWriter(outputPath, true)) {

          long startTime = System.nanoTime();
          cercleMinNaif(points);
          long endTime = System.nanoTime();

          long duration = (endTime - startTime); //durée en nanosecondes
          double durationInMilliseconds = duration / 1e6; //convertit en millisecondes

          writer.write(filename + " " + durationInMilliseconds + " ms\n");
          System.out.println(durationInMilliseconds + " ms");
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
  
  public static void WriteMemoryUsageNaifToFile(String filePath, String filename, String outputMemoryPath) {
      ArrayList<Point> points = readPointsFromFile(filePath + filename);

      try (FileWriter memoryWriter = new FileWriter(outputMemoryPath, true)) {

          //calcul de la mémoire utilisée
          Runtime runtime = Runtime.getRuntime();
          runtime.gc(); // appel du garbage collector pour une mesure plus précise
          long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
          cercleMinNaif(points);
          long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
          long memoryUsed = memoryAfter - memoryBefore;

          memoryWriter.write(filename + " " + memoryUsed + " bytes\n");

          System.out.println("Memory Usage = " + memoryUsed + " bytes");
          
      }catch (IOException e) {
          e.printStackTrace();
      }
  }
  
  public static void WriteMemoryUsageBMDToFile(String filePath, String filename, String outputMemoryPath) {
      ArrayList<Point> points = readPointsFromFile(filePath + filename);

      try (FileWriter memoryWriter = new FileWriter(outputMemoryPath, true)) {

          //calcul de la mémoire utilisée
          Runtime runtime = Runtime.getRuntime();
          runtime.gc(); // appel du garbage collector pour une mesure plus précise
          long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
          b_md(points, new ArrayList<Point>());
          long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
          long memoryUsed = memoryAfter - memoryBefore;

          memoryWriter.write(filename + " " + memoryUsed + " bytes\n");

          System.out.println("Memory Usage = " + memoryUsed + " bytes");
          
      }catch (IOException e) {
          e.printStackTrace();
      }
  }
  
  public static void calculateCPUFilesFromDirectory(String directoryPath, String outputPathNaif,String outputPathBMD ) {
      File dir = new File(directoryPath);

      //filtre qui sélectionne que les fichiers qui correspondent au motif "test-*.points"
      FilenameFilter filter = (dir1, name) -> name.matches("test-\\d+\\.points");

      String[] files = dir.list(filter);

      if (files != null) {
          //trie les fichiers par ordre numérique
          Arrays.sort(files, (f1, f2) -> {
              int num1 = Integer.parseInt(f1.replace("test-", "").replace(".points", ""));
              int num2 = Integer.parseInt(f2.replace("test-", "").replace(".points", ""));
              return Integer.compare(num1, num2);
          });

          //itére sur chaque fichier, en excluant "test-1.points"
          for (String fileName : files) {
              int fileNumber = Integer.parseInt(fileName.replace("test-", "").replace(".points", ""));
              if (fileNumber > 1) {
                  System.out.println("Processing " + fileName);
                  
                  WriteCPUTimeBMDToFile(directoryPath, fileName, outputPathBMD);
                  WriteCPUTimeNaifToFile(directoryPath, fileName, outputPathNaif);
                  
//                  WriteMemoryUsageNaifToFile(directoryPath, fileName, outputPathNaif);
//                  WriteMemoryUsageBMDToFile(directoryPath, fileName, outputPathBMD);
              }
          }
      } else {
          System.out.println("Pas de fichier trouvé correspondant au filtre");
      }
  }
  
  public static void calculerMoyenneEcartFrequenceTempsCPU(String fichierEntree, String fichierSortieMoyenne, String fichierSortieEcart, String fichierSortieFreq) {
      try (BufferedReader reader = new BufferedReader(new FileReader(fichierEntree));
           FileWriter writer = new FileWriter(fichierSortieMoyenne)) {
          String ligne;
          double sommeTemps = 0;
          int nombreDeValeurs = 0;
          ArrayList<Double> tempsMesures = new ArrayList<>();
          Map<Double, Integer> frequences = new HashMap<>();

          while ((ligne = reader.readLine()) != null) {
              String[] parties = ligne.split(" ");
              String tempsEnMs = parties[1];
              double temps = Double.parseDouble(tempsEnMs);
              tempsMesures.add(temps);
              frequences.put(temps, frequences.getOrDefault(temps, 0) + 1);
              sommeTemps += temps;
              nombreDeValeurs++;
          }

          double moyenne = nombreDeValeurs > 0 ? sommeTemps / nombreDeValeurs : 0;
          writer.write(moyenne + " ms\n");
          System.out.println("Moyenne des temps calculée et écrite dans " + fichierSortieMoyenne);
          
          double sommeDesEcartCarres = 0;

          for (double temps : tempsMesures) {
              sommeDesEcartCarres += Math.pow(temps - moyenne, 2);
          }
          
          double ecartType = Math.sqrt(sommeDesEcartCarres / nombreDeValeurs);
          
          try (FileWriter writer2 = new FileWriter(fichierSortieEcart)) {
              writer2.write(ecartType + " ms\n");
              System.out.println("Écart type des temps calculé et écrit dans " + fichierSortieEcart);
          }
          
          ArrayList<Double> sortedKeys = new ArrayList<>(frequences.keySet());
          Collections.sort(sortedKeys);
          
          try (FileWriter writer3 = new FileWriter(fichierSortieFreq)) {
        	  for (double key : sortedKeys) {
                  writer3.write(key + " ms: " + frequences.get(key) + "\n");
        	  }
              System.out.println("Données du diagramme de fréquences écrites dans " + fichierSortieFreq);

          }

      } catch (IOException e) {
          e.printStackTrace();
      }
  }
  
  
  public static void calculerMoyenneMemoire(String fichierEntree, String fichierSortie) {
      try (BufferedReader reader = new BufferedReader(new FileReader(fichierEntree));
           FileWriter writer = new FileWriter(fichierSortie)) {
          String ligne;
          long sommeMemoire = 0;
          int nombreDeValeurs = 0;

          while ((ligne = reader.readLine()) != null) {
              String[] parties = ligne.split(" ");
              if (parties.length == 3) {
                  String memoireEnBytes = parties[1];
                  long memoire = Long.parseLong(memoireEnBytes);
                  sommeMemoire += memoire;
                  nombreDeValeurs++;
              }
          }

          double moyenne = nombreDeValeurs > 0 ? (double)sommeMemoire / nombreDeValeurs : 0;
          writer.write(String.format("%.2f bytes\n", moyenne));
          System.out.println("Moyenne mémoire calculée et écrite dans " + fichierSortie);

      } catch (IOException e) {
          e.printStackTrace();
      }
  }
  
  
  public static void main(String[] args) {
	  System.out.println(System.getProperty("user.dir"));
      String testsDirectoryPath = "/home/fan/Documents/M1S2/CPA/TME1_cpa2023/Varoumas_benchmark/samples/";
      String filePathTimeNaif = "/home/fan/Documents/M1S2/CPA/TME1_cpa2023/temps_cpu_naif.txt";
      String filePathTimeBMD = "/home/fan/Documents/M1S2/CPA/TME1_cpa2023/temps_cpu_bmd.txt";
      
      String filePathMemoryNaif = "/home/fan/Documents/M1S2/CPA/TME1_cpa2023/memory_naif.txt";
      String filePathMemoryBMD = "/home/fan/Documents/M1S2/CPA/TME1_cpa2023/memory_bmd.txt";
      
      String filePathTimeNaifMean = "/home/fan/Documents/M1S2/CPA/TME1_cpa2023/temps_cpu_naif_mean.txt";
	  String filePathTimeBMDMean = "/home/fan/Documents/M1S2/CPA/TME1_cpa2023/temps_cpu_bmd_mean.txt";
	  
	  String filePathTimeNaifEcart = "/home/fan/Documents/M1S2/CPA/TME1_cpa2023/temps_cpu_naif_ecart.txt";
	  String filePathTimeBMDEcart = "/home/fan/Documents/M1S2/CPA/TME1_cpa2023/temps_cpu_bmd_ecart.txt";
	  
	  String filePathTimeNaifFreq = "/home/fan/Documents/M1S2/CPA/TME1_cpa2023/temps_cpu_naif_freq.txt";
	  String filePathTimeBMDFreq = "/home/fan/Documents/M1S2/CPA/TME1_cpa2023/temps_cpu_bmd_freq.txt";
	  
	  String filePathMemoryNaifMean = "/home/fan/Documents/M1S2/CPA/TME1_cpa2023/memory_naif_mean.txt";
	  String filePathMemoryBMDMean = "/home/fan/Documents/M1S2/CPA/TME1_cpa2023/memory_bmd_mean.txt";

//      calculateCPUFilesFromDirectory(testsDirectoryPath, filePathTimeNaif, filePathTimeBMD);
//      calculateCPUFilesFromDirectory(testsDirectoryPath, filePathMemoryNaif, filePathMemoryBMD);
	  
	  calculerMoyenneEcartFrequenceTempsCPU(filePathTimeNaif, filePathTimeNaifMean, filePathTimeNaifEcart, filePathTimeNaifFreq);
	  calculerMoyenneEcartFrequenceTempsCPU(filePathTimeBMD, filePathTimeBMDMean, filePathTimeBMDEcart, filePathTimeBMDFreq);

//      calculerMoyenneMemoire(filePathMemoryNaif, filePathMemoryNaifMean);
//      calculerMoyenneMemoire(filePathMemoryBMD, filePathMemoryBMDMean);

  }
}
