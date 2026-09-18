package com.mycompany.mvcweb.controlador;

import com.mycompany.mvcweb.dao.ProductoDAO;
import com.mycompany.mvcweb.modelo.Producto;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
public class ProductoControlador {

    private ProductoDAO productoDAO = new ProductoDAO();

    @GetMapping
    public String listar(Model model) {

        model.addAttribute("productos", productoDAO.listarTodos());

        if (!model.containsAttribute("producto")) {
            model.addAttribute(
                    "producto",
                    new Producto(0, 0, "", 0, 0)
            );
        }

        return "productos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) {

        model.addAttribute("productos", productoDAO.listarTodos());
        model.addAttribute("producto", productoDAO.buscarPorId(id));

        return "productos";
    }

    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute Producto producto,
            RedirectAttributes ra) {

        try {

            boolean ok;

            if (producto.getIdProducto() == 0) {
                ok = productoDAO.insertar(producto);
            } else {
                ok = productoDAO.actualizar(producto);
            }

            if (!ok) {
                ra.addFlashAttribute(
                        "error",
                        "Error al momento de almacenar el producto"
                );
            }

        } catch (Exception e) {

            ra.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/productos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id) {

        Producto producto = productoDAO.buscarPorId(id);

        if (producto != null) {
            productoDAO.eliminar(producto);
        }

        return "redirect:/productos";
    }
}