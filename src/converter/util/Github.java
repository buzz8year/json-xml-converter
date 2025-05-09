package converter.util;

import converter.file.Writer;
import converter.node.CallbackIterator;
import converter.node.Node;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Github
{
    private static final String GITHUB_LOGIN = "xxx";
    private static final String API_TOKEN = "ghp_xxx";

    private static final String WEB_URL = "https://github.com/";
    private static final String API_URL_USER = "https://api.github.com/user";
    private static final String API_FORMAT_FOLLOWING = "https://api.github.com/users/%s/following?per_page=100&page=%d";
    private static final String API_FORMAT_FOLLOWERS = "https://api.github.com/users/%s/followers?per_page=100&page=%d";
    private static final String API_FORMAT_IS_FOLLOWING_ME = "https://api.github.com/users/%s/following/%s";

    private static final String FILE_FOLLOWERS_NOT = "github-not-followers.txt";
    private static final String FILE_FOLLOWERS = "github-followers.json";
    private static final String FILE_FOLLOWING = "github-following.json";

    @SneakyThrows
    public static String request(String url)
    {
        URI uri = URI.create(url);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET()
                .header("Authorization", "Bearer " + API_TOKEN)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String requestMe()
    {
        return request(API_URL_USER);
    }

    public static void requestFollowing() throws IOException
    {
        requestPaginated(API_FORMAT_FOLLOWING, FILE_FOLLOWING);
    }

    public static void requestFollowers() throws IOException
    {
        requestPaginated(API_FORMAT_FOLLOWERS, FILE_FOLLOWERS);
    }

    public static void requestPaginated(String format, String file) throws IOException
    {
        List<String> data = new ArrayList<>();
        String body;

        int page = 1;
        while (!(body = request(String.format(format, GITHUB_LOGIN, page))).trim().equals("[]"))
        {
            data.add(JsonString.trimBrackets(body));
            page++;
        }
        Writer.append(JsonString.wrapBrackets(String.join(",", data)), file);
    }

    @SneakyThrows
    public static void requestNotFollowersByNode(Node node)
    {
        if (!node.name.equals("login"))
            return;

        // NOTE: API url to find out if user follows you
        String url = String.format(API_FORMAT_IS_FOLLOWING_ME, node.value, GITHUB_LOGIN);
        String body = request(url);

        // NOTE: body.isBlank() means 204 no-error answer (follower),
        // else answer is 404 (not found, not follower) which we are looking for
        if (!body.isBlank())
            Writer.append(String.format("%s%s%n", WEB_URL, node.value), FILE_FOLLOWERS_NOT);
    }

    @SneakyThrows
    public static void searchTreeForNotFollowers(List<Node> tree)
    {
        CallbackIterator iterator = new CallbackIterator(2);
        iterator.setCallback(Github::requestNotFollowersByNode);
        iterator.iterateTree(tree);
    }

}
