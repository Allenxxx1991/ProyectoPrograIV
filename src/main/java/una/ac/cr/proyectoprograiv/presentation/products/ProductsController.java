package una.ac.cr.proyectoprograiv.presentation.products;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import una.ac.cr.proyectoprograiv.logic.FotoProducto;
import una.ac.cr.proyectoprograiv.logic.Producto;
import una.ac.cr.proyectoprograiv.logic.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Controller("products")
public class ProductsController {
    @Autowired
    private Service service;

    @Autowired
    private HttpSession httpSession;

    @Value("${upload.dir}")
    private String uploadDir;
    @PostMapping("/products/update")
    public String updateProduct(@RequestParam("id") int id,
                                @RequestParam("nombre") String nombre,
                                @RequestParam("descripcion") String descripcion,
                                @RequestParam("precio") Double precio,
                                @RequestParam("fotos") List<MultipartFile> files,
                                Model model) {
        Optional<Producto> optionalProducto = service.productoFindById(id);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(BigDecimal.valueOf(precio));
            service.productoSave(producto);

            // Manejo de las fotos (si es necesario subir nuevas fotos)
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                try {
                    Files.createDirectories(uploadPath);
                } catch (IOException e) {
                    model.addAttribute("error", "No se pudo crear el directorio de subida.");
                    return "presentation/products/ViewEditProducts";
                }
            }

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path filePath = uploadPath.resolve(fileName);
                    try {
                        Files.copy(file.getInputStream(), filePath);
                        FotoProducto foto = new FotoProducto();
                        foto.setIdProducto(producto);
                        foto.setFoto(fileName);
                        service.fotoProductoSave(foto);
                    } catch (IOException e) {
                        model.addAttribute("error", "Error al guardar el archivo.");
                        return "/presentation/products/ViewEditProduct";
                    }
                }
            }
        }
        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") int id, Model model) {
        Optional<Producto> optionalProducto = service.productoFindById(id);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            model.addAttribute("producto", producto);
            return "presentation/products/ViewEditProducts";  // Cargar la vista de edición
        }
        return "redirect:/products";  // Si no se encuentra el producto, redirige a la lista de productos.
    }

    @GetMapping("/products")
    public String products(Model model) {
        // Obtener todos los productos
        Iterable<Producto> productosIterable = service.productoFindAll();
        List<Producto> productos = new ArrayList<>();
        productosIterable.forEach(productos::add);  // Convertir a List

        // Agregar la lista de productos al modelo
        model.addAttribute("productos", productos);

        // Crear un nuevo producto para el formulario (si es necesario)
        Producto producto = new Producto();
        FotoProducto foto = new FotoProducto();
        model.addAttribute("producto", producto);
        model.addAttribute("foto", foto);

        // Retornar la vista que mostrará la lista y el formulario
        return "presentation/products/ProductList";  // Cambia esto por la vista adecuada
    }

    @PostMapping("/products/upload")
    public String uploadProduct(@RequestParam("nombre") String nombre,
                                @RequestParam("descripcion") String descripcion,
                                @RequestParam("precio") Double precio,
                                @RequestParam("fotos") List<MultipartFile> files,
                                Model model) {
        try {
            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(BigDecimal.valueOf(precio));
            service.productoSave(producto);

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath);

                FotoProducto foto = new FotoProducto();
                foto.setIdProducto(producto);
                foto.setFoto(fileName);
                service.fotoProductoSave(foto);
            }

        } catch (IOException e) {
            model.addAttribute("error", "Ocurrió un error durante el inicio de sesión.");
            return "/presentation/products/ViewEditProducts";
        }

        return "redirect:/products";
    }

}
