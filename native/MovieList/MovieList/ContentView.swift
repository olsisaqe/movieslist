import SwiftUI

struct ContentView: View {
    @ObservedObject var store: MoviesStore

    var body: some View {
         NavigationView {
            List(store.movies, id: \.movieId) { movie in
                HStack(alignment: .center) {
                    ImageView(withURL: movie.image!)
                    VStack(alignment: .leading) {
                        Text(movie.title!)
                        Text(movie.subtitle!)
                    }
                }
            }
        }
            .onAppear(perform: store.fetch)
            .navigationBarTitle(Text("Movies"))
    }
}
