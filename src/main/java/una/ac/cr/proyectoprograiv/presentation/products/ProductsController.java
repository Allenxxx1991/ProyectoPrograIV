package una.ac.cr.proyectoprograiv.presentation.products;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
import java.util.List;

@org.springframework.stereotype.Controller("products")
public class ProductsController {
    @Autowired
    private Service service;

    @Autowired
    private HttpSession httpSession;

    @Value("${upload.dir}")
    private String uploadDir;


    @GetMapping("/products")
    public String products(Model model) {
        Producto producto = new Producto();
        FotoProducto foto = new FotoProducto();
        model.addAttribute("producto", producto);
        model.addAttribute("foto", foto);
        return "/presentation/products/ViewProductos";
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
            return "presentation/login/ViewLogin";
        }

        return "redirect:/products";
    }
}
