package com.mycompany.mvcweb.controlador;

import com.mycompany.mvcweb.dao.MarcaDAO;
import com.mycompany.mvcweb.modelo.Marca;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/marcas")
public class MarcaControlador {

    private MarcaDAO marcaDAO = new MarcaDAO();

    @GetMapping
    public String listar(Model model) {

        model.addAttribute("marcas", marcaDAO.listarTodos());

        if (!model.containsAttribute("marca")) {
            model.addAttribute("marca", new Marca(0, ""));
        }

        return "marcas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) {

        model.addAttribute("marcas", marcaDAO.listarTodos());
        model.addAttribute("marca", marcaDAO.buscarPorId(id));

        return "marcas";
    }

    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute Marca marca,
            RedirectAttributes ra) {

        try {

            boolean ok;

            if (marca.getIdMarca() == 0) {
                ok = marcaDAO.insertar(marca);
            } else {
                ok = marcaDAO.actualizar(marca);
            }

            if (!ok) {
                ra.addFlashAttribute(
                        "error",
                        "Error al momento de almacenar la marca"
                );
            }

        } catch (Exception e) {

            ra.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/marcas";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id) {

        Marca marca = marcaDAO.buscarPorId(id);

        if (marca != null) {
            marcaDAO.eliminar(marca);
        }

        return "redirect:/marcas";
    }
}