/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be;

/**
 *
 * @author alexl
 */
public class User
{
    private int id;
    private String name;
    private String password;

    public User(int id, String name, String password)
    {
        this.id = id;
        this.name = name;
        this.password = password;
    }
    
    public User(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + '}';
    }
}
