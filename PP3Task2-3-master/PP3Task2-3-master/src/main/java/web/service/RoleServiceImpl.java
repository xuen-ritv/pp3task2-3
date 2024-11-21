package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.model.Role;
import web.repositories.RoleRep;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRep roleRep;

    @Autowired
    public RoleServiceImpl(RoleRep roleRep) {
        this.roleRep = roleRep;
    }
    @Transactional
    @Override
    public List<Role> findAll() {
        return roleRep.findAll();
    }
    @Override
    public Role getRoleById(Long id) {
        return roleRep.findById(Math.toIntExact(id)).orElse(null);
    }

    @Override
    public Role findByName(String name) {
        return roleRep.findByName(name);
    }
}
