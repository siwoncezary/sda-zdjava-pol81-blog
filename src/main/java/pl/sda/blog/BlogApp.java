package pl.sda.blog;

import pl.sda.blog.entity.Article;
import pl.sda.blog.repository.ArticleRepository;
import pl.sda.blog.repository.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class BlogApp {
    static Scanner scanner = new Scanner(System.in);
    static EntityManagerFactory factory = Persistence.createEntityManagerFactory("blog");
    static ArticleRepository articleRepo = new ArticleRepository(factory);
    static private void printMenu(){
        System.out.println("1. Dodaj artykuł");
        System.out.println("2. Zmień tytuł artykułu");
        System.out.println("3. Usuń artykuł");
        System.out.println("4. Lista artykułów");
        System.out.println("5. Wyszukaj artykuły dla autora"); // nowa opcja
        System.out.println("0. Koniec");
    }

    static private void addArticle(){
        scanner.nextLine();
        System.out.println("Wpisz tytuł:");
        String title = scanner.nextLine();
        System.out.println("Wpisz autora:");
        String author = scanner.nextLine();
        Article article = new Article(0, author, title, "XXX", null, 0);
        //articleRepo.save(article);
        articleRepo.merge(article);
    }
    static private void changeTitle(){
        scanner.nextLine();
        System.out.println("Wpisz id");
        long id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Wpisz tytuł: ");
        String title = scanner.nextLine();
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Article art =  em.find(Article.class, id);
        if (art != null) {
            System.out.println("Changed title");
            art.setTitle(title);
        }
        em.getTransaction().commit();
    }
    static private void changeTitleByMerge(){
        scanner.nextLine();
        System.out.println("Wpisz id");
        long id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Wpisz tytuł: ");
        String title = scanner.nextLine();
        Article art = articleRepo.findById(id);
        if(art == null){
            System.out.println("Brak takiego artykułu!");
            return;
        }
        art.setTitle(title);
        articleRepo.merge(art);
    }

    static private void printAllArticles(){
        articleRepo.findAll().forEach(System.out::println);
    }

    public static void main(String[] args) {
        //System.out.println(articleRepo.sumAllRatingForAuthor("Tomek"));
        while(true){
            printMenu();
            if(scanner.hasNextInt()){
                switch (scanner.nextInt()){
                    case 1:
                        addArticle();
                        break;
                    case 2:
                        changeTitleByMerge();
                        break;
                    case 3:
                        break;
                    case 4:
                        printAllArticles();
                        break;
                    case 0:
                        return;
                }
            } else {
                scanner.nextLine();
            }
        }
    }
}
