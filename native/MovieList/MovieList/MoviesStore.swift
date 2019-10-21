import Combine
import core

final class MoviesStore: ObservableObject {
    @Published var movies: [Movie] = []
    private let moviesService = MoviesServiceCallback(moviesRepository: MoviesRest())
    
    
    func fetch() {
        moviesService.getMovies {
            self.movies = $0
        }
    }
}
