package scheduler;

import dto.RegionDTO;
import dto.ProvinceDTO;
import dto.ReportDTO;
import service.CovidDataService;
import repository.DatabaseHelper;

import java.util.List;
import java.util.Scanner;

public class CovidScheduler {

    // Método principal para ejecutar la lógica
    public static void executeMainLogic() {
        // Inicializa el servicio
        CovidDataService covidDataService = new CovidDataService();
        Scanner scanner = new Scanner(System.in);

        try {
            // Paso 1: Mostrar regiones y permitir seleccionar una
            List<RegionDTO> regions = covidDataService.fetchRegions();
            RegionDTO selectedRegion = displayItemsWithPagination(regions, "Regiones", 10, scanner);

            DatabaseHelper dbHelper = new DatabaseHelper(); 
            for (RegionDTO region : regions) {
                dbHelper.saveRegion(region.getIso(), region.getName()); 
            }

            List<ProvinceDTO> provsave = covidDataService.fetchProvinces(selectedRegion.getIso());
            for (ProvinceDTO province : provsave) {
                dbHelper.saveProvince(province.getIso(), province.getCode(), province.getProvince());
            }

            if (selectedRegion == null) {
                System.out.println("No se seleccionó ninguna región. La aplicación se detendrá.");
                return;
            }

            System.out.println("Región seleccionada: " + selectedRegion.getName());

            // Paso 2: Mostrar provincias para la región seleccionada
            List<ProvinceDTO> provinces = covidDataService.fetchProvinces(selectedRegion.getIso());
            ProvinceDTO selectedProvince = displayItemsWithPagination(provinces, "Provincias", 10, scanner);

            if (selectedProvince == null) {
                System.out.println("No se seleccionó ninguna provincia. La aplicación se detendrá.");
                return;
            }

            // Paso 3: Mostrar reportes para la provincia seleccionada
            String date = java.time.LocalDate.now().toString();
            System.out.println(selectedProvince.getCode());
            List<ReportDTO> reports = covidDataService.fetchReports(selectedProvince.getIso(), date);
            
            displayReports(reports);

        } catch (Exception e) {
            System.err.println("Error durante la ejecución: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    // Método para mostrar una lista de elementos con paginación
    private static <T> T displayItemsWithPagination(List<T> items, String itemType, int pageSize, Scanner scanner) {
        int totalPages = (int) Math.ceil((double) items.size() / pageSize);
        int currentPage = 1;
        T selectedItem = null;

        while (true) {
            System.out.println("\n--- " + itemType + " - Página " + currentPage + " de " + totalPages + " ---");
            // Mostrar el encabezado
            if (itemType.equals("Regiones")) {
                System.out.println("CODIGO   | ISO    | NOMBRE");
            } else if (itemType.equals("Provincias")) {
                System.out.println("CODIGO   | ISO    | NOMBRE                             | LAT                | LONG");
            }

            // Calcular el rango de elementos para la página actual
            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, items.size());

            // Mostrar los elementos de la página actual
            for (int i = startIndex; i < endIndex; i++) {
                if (items.get(i) instanceof RegionDTO) {
                    System.out.println(((RegionDTO) items.get(i)).toFormattedString(i + 1));
                } else if (items.get(i) instanceof ProvinceDTO) {
                    System.out.println(((ProvinceDTO) items.get(i)).toFormattedString(i + 1));
                }
            }

            // Opciones del usuario
            System.out.println("\nOPCIONES:| 1. Seleccionar un elemento | 2. Siguiente página | 3. Página anterior |4. Salir");

            System.out.print("INGRESE UNA OPCION: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1": // Seleccionar un elemento
                    System.out.print("INGRESE EL CODIGO: ");
                    String itemNumberInput = scanner.nextLine();
                    try {
                        int itemNumber = Integer.parseInt(itemNumberInput);
                        if (itemNumber >= 1 && itemNumber <= items.size()) {
                            selectedItem = items.get(itemNumber - 1);
                            return selectedItem;
                        } else {
                            System.out.println("Número de elemento inválido. Intente nuevamente.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida. Intente nuevamente.");
                    }
                    break;

                case "2": // Siguiente página
                    if (currentPage < totalPages) {
                        currentPage++;
                    } else {
                        System.out.println("Ya estás en la última página.");
                    }
                    break;

                case "3": // Página anterior
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        System.out.println("Ya estás en la primera página.");
                    }
                    break;

                case "4": // Salir
                    return null;

                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    // Método para mostrar los reportes en la consola
    private static void displayReports(List<ReportDTO> reports) {
        System.out.println("\n--- Reportes ---");
        if (reports.isEmpty()) {
            System.out.println("No hay reportes disponibles.");
            return;
        }

        for (ReportDTO report : reports) {
            System.out.println(report.toString());
        }
    }

    public static void main(String[] args) {
        executeMainLogic(); // Ejecutar la lógica principal
    }
}
