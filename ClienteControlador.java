package com.mycompany.mvcweb.controlador;

import com.mycompany.mvcweb.dao.ClienteDAO;
import com.mycompany.mvcweb.modelo.Cliente;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
public class ClienteControlador {

    private ClienteDAO clienteDAO = new ClienteDAO();

    @GetMapping
    public String listar(Model model) {

        model.addAttribute("clientes", clienteDAO.listarTodos());

        if (!model.containsAttribute("cliente")) {
            model.addAttribute("cliente", new Cliente(0, "", "", "", ""));
        }

        return "clientes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) {

        model.addAttribute("clientes", clienteDAO.listarTodos());
        model.addAttribute("cliente", clienteDAO.buscarPorId(id));

        return "clientes";
    }

    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute Cliente cliente,
            RedirectAttributes ra) {

        try {

            boolean ok;

            if (cliente.getIdCliente() == 0) {
                ok = clienteDAO.insertar(cliente);
            } else {
                ok = clienteDAO.actualizar(cliente);
            }

            if (!ok) {
                ra.addFlashAttribute(
                        "error",
                        "Error al momento de almacenar el cliente"
                );
            }

        } catch (Exception e) {

            ra.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/clientes";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id) {

        Cliente cliente = clienteDAO.buscarPorId(id);

        if (cliente != null) {
            clienteDAO.eliminar(cliente);
        }

        return "redirect:/clientes";
    }
}