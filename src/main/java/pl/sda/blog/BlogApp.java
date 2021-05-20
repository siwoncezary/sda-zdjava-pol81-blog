package pl.sda.blog;

import pl.sda.blog.entity.*;
import pl.sda.blog.repository.JpaAuthorRepository;
import pl.sda.blog.repository.JpaRepository;
import pl.sda.blog.repository.JpaTagRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class BlogApp {
    static Scanner scanner = new Scanner(System.in);
    static EntityManagerFactory factory = Persistence.createEntityManagerFactory("blog");
    static JpaRepository<Article, Long> articleRepo = new JpaRepository<>(factory, Article.class);
    static JpaAuthorRepository authors = new JpaAuthorRepository(factory, Author.class);
    static JpaTagRepository tags = new JpaTagRepository(factory, Tag.class);

    static private void printMenu() {
        System.out.println("1. Dodaj artykuł");
        System.out.println("2. Zmień tytuł artykułu");
        System.out.println("3. Usuń artykuł");
        System.out.println("4. Lista artykułów");
        System.out.println("5. Wyszukaj artykuły dla taga"); // nowa opcja
        System.out.println("6. Dodaj autora");
        System.out.println("7. Lista autorów");
        System.out.println("0. Koniec");
    }

    static private void findByTag(){
        scanner.nextLine();
        System.out.println("Wpisz id taga:");
        long id = scanner.nextLong();
        System.out.println(tags.findArticleByTag(id));
    }

    static private void removeArticleById(){
        scanner.nextLine();
        articleRepo.findAll().forEach(a -> System.out.println(a.getId() +" " + a.getTitle()));
        System.out.println("Wpisz id artykułu:");
        long id = scanner.nextLong();
        articleRepo.deleteById(id);
    }

    static private void printAuthors() {
        authors.findAll().forEach(author -> {
            System.out.println("Nick: " + author.getNick() + ", email: " + author.getAddress());
        });
    }

    static private void addArticle() {
        scanner.nextLine();
        System.out.println("Wpisz tytuł:");
        String title = scanner.nextLine();
        System.out.println("Wpisz tagi:");
        List<String> tagLabel = new ArrayList<>();
        //TODO napisać samodzielnie dodawanie istniejących tagów do artykułu
//        while(true){
//            Wczytywanie tagów i dodawanie do tagLabel
//        }
        //Przefiltrować, czy w repozytorium znajdują się tagi o label w tagLabel
        //tags.findAll().stream().
        System.out.println("Wpisz nick autora:");
        authors.findAll().forEach(a -> System.out.println(a.getNick()));
        String authorNick = scanner.nextLine();
        Optional<Author> byNick = authors.findByNick(authorNick);
        byNick.ifPresent(author -> {
            Article article = Article.builder().author(author).title(title).build();
            articleRepo.merge(article);
        });
    }

    static private void changeTitle() {
        scanner.nextLine();
        System.out.println("Wpisz id");
        long id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Wpisz tytuł: ");
        String title = scanner.nextLine();
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Article art = em.find(Article.class, id);
        if (art != null) {
            System.out.println("Changed title");
            art.setTitle(title);
        }
        em.getTransaction().commit();
    }

    static private void changeTitleByMerge() {
        scanner.nextLine();
        System.out.println("Wpisz id");
        long id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Wpisz tytuł: ");
        String title = scanner.nextLine();
        Optional<Article> art = articleRepo.findById(id);
        art.ifPresent(article -> {
            article.setTitle(title);
            articleRepo.save(article);
        });
        if (art.isEmpty()) {
            System.out.println("Brak takiego artykułu!");
        }
    }

    static private void printAllArticles() {
        articleRepo.findAll().forEach(article -> {
                    System.out.println("nick autora "
                            + article.getAuthor().getNick() + ", tytuł "
                            + article.getTitle() + ", rating "
                            + article.getRating() + ", tags "
                            + article.getTags());
                }
        );
    }

    public static void main(String[] args) {
        insertData();
        while (true) {
            printMenu();
            if (scanner.hasNextInt()) {
                switch (scanner.nextInt()) {
                    case 1:
                        addArticle();
                        break;
                    case 2:
                        changeTitleByMerge();
                        break;
                    case 3:
                        removeArticleById();
                        break;
                    case 4:
                        printAllArticles();
                        break;
                    case 5:
                        findByTag();
                        break;
                    case 6:
                        //TODO dodać wywołanie funkcji dodającej autora
                        break;
                    case 7:
                        printAuthors();
                    case 0:
                        return;
                }
            } else {
                scanner.nextLine();
            }
        }
    }

    private static void insertData() {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();

        Address location = Address.builder().city("Warsaw").street("Marszałkowska 24/5").build();
        em.persist(location);
        Author author1 = Author.builder()
                .nick("tom")
                .firstName("Tomasz")
                .lastName("Nowak")
                .address(new EmailAddress("tom@.op.pl"))
                .location(location)
                .build();
        em.persist(author1);
        //Dodajemy autora z nowym adresem, działa tylko jeśli jest ustawiona kaskada w autorze typu ALL lub PERSIST
        Author author2 = Author.builder()
                .nick("alex")
                .lastName("All")
                .firstName("Alex")
                .location(Address.builder().city("New Yor").street("Avenue 14/5").build())
                .address(new EmailAddress("alex@ny.org"))
                .build();
        em.persist(author2);
        //przykład usuwania location, gdy usuwany jest autor: działa dla kaskady REMOVE i ALL
        //em.remove(author1);
        //Address address = em.find(Address.class, location.getId());
        //System.out.println(address);
        Tag tag1 = Tag.builder().label("Java").build();
        Tag tag2 = Tag.builder().label("Programming").build();
        Tag tag3 = Tag.builder().label("Jdbc").build();
        em.persist(tag1);
        em.persist(tag2);
        em.persist(tag3);
        Article art1 = Article.builder().title("Java dla odważnych").rating(125).content("XXX").author(author1).build();
        Article art2 = Article.builder().title("Jdbc w 5 smakach").rating(155).content("YYY").author(author2).build();
        Article art3 = Article.builder().title("Hibernacja danych").rating(25).content("BBBB").author(author1).build();
        art1.addTag(tag1);
        art1.addTag(tag3);
        art2.addTag(tag2);
        art2.addTag(tag3);
        art3.addTag(tag1);
        art3.addTag(tag2);

        em.persist(art1);
        em.persist(art2);
        em.persist(art3);

        //Dodaj nowy artykuł nowego autor, który ma nowy address - location
//        System.out.println(em.createQuery("select t from Tag t join Article a").getResultList());
        em.getTransaction().commit();
        em.close();
    }
}
