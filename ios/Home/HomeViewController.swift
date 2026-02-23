import UIKit
import Combine

final class HomeViewController: UIViewController {

    private let viewModel = HomeViewModel()
    private var cancellables = Set<AnyCancellable>()

    private let avatarView: UIImageView = {
        let iv = UIImageView()
        iv.translatesAutoresizingMaskIntoConstraints = false
        iv.contentMode = .scaleAspectFill
        iv.clipsToBounds = true
        iv.layer.cornerRadius = 32
        iv.backgroundColor = UIColor(white: 1, alpha: 0.08)
        iv.image = UIImage(named: "ic_placeholder_user")
        NSLayoutConstraint.activate([
            iv.heightAnchor.constraint(equalToConstant: 64),
            iv.widthAnchor.constraint(equalTo: iv.heightAnchor)
        ])
        return iv
    }()

    private let greetingLabel: UILabel = {
        let l = UILabel()
        l.translatesAutoresizingMaskIntoConstraints = false
        l.textColor = AppColors.white
        l.font = AppFont.title()
        l.text = "Hello, Guest!"
        return l
    }()

    private let userTypeLabel: UILabel = {
        let l = UILabel()
        l.translatesAutoresizingMaskIntoConstraints = false
        l.textColor = AppColors.hint
        l.font = AppFont.small()
        l.text = "Unknown Type"
        return l
    }()

    private lazy var userCard: UIStackView = {
        let labels = UIStackView(arrangedSubviews: [greetingLabel, userTypeLabel])
        labels.axis = .vertical
        labels.spacing = 4
        let h = UIStackView(arrangedSubviews: [avatarView, labels])
        h.translatesAutoresizingMaskIntoConstraints = false
        h.axis = .horizontal
        h.alignment = .center
        h.spacing = 12
        return h
    }()

    private var collection: UICollectionView!
    private var dataSource: UICollectionViewDiffableDataSource<Int, AdViewData>!
    private let spinner: UIActivityIndicatorView = {
        let s = UIActivityIndicatorView(style: .large)
        s.translatesAutoresizingMaskIntoConstraints = false
        s.hidesWhenStopped = true
        return s
    }()

    private var autoScrollTimer: Timer?
    private let autoScrollInterval: TimeInterval = 4.0

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = AppColors.bg
        title = "Home"

        setupCollection()
        layout()
        bind()
        viewModel.start()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        startAutoScroll()
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        stopAutoScroll()
    }

    private func setupCollection() {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        layout.minimumLineSpacing = 12
        layout.sectionInset = .zero

        collection = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collection.translatesAutoresizingMaskIntoConstraints = false
        collection.backgroundColor = .clear
        collection.isPagingEnabled = true
        collection.showsHorizontalScrollIndicator = false
        collection.contentInsetAdjustmentBehavior = .never
        collection.delegate = self
        collection.register(AdCell.self, forCellWithReuseIdentifier: AdCell.reuseID)

        dataSource = UICollectionViewDiffableDataSource<Int, AdViewData>(collectionView: collection) { (cv, indexPath, item) in
            let cell = cv.dequeueReusableCell(withReuseIdentifier: AdCell.reuseID, for: indexPath) as! AdCell
            cell.configure(with: item.visual)
            return cell
        }
    }

    private func layout() {
        let vstack = UIStackView(arrangedSubviews: [userCard, collection, spinner])
        vstack.axis = .vertical
        vstack.spacing = 16
        vstack.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(vstack)

        NSLayoutConstraint.activate([
            vstack.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: 16),
            vstack.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant: -16),
            vstack.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            collection.heightAnchor.constraint(equalTo: view.heightAnchor, multiplier: 0.28)
        ])
    }

    private func bind() {
        viewModel.$greeting
            .receive(on: DispatchQueue.main)
            .sink { [weak self] in self?.greetingLabel.text = $0 }
            .store(in: &cancellables)

        viewModel.$userTypeText
            .receive(on: DispatchQueue.main)
            .sink { [weak self] in self?.userTypeLabel.text = $0 }
            .store(in: &cancellables)

        viewModel.$avatar
            .receive(on: DispatchQueue.main)
            .sink { [weak self] img in
                self?.avatarView.image = img ?? UIImage(named: "ic_placeholder_user")
            }
            .store(in: &cancellables)

        viewModel.$isLoadingAds
            .receive(on: DispatchQueue.main)
            .sink { [weak self] loading in
                loading ? self?.spinner.startAnimating() : self?.spinner.stopAnimating()
                self?.collection.isHidden = loading
            }
            .store(in: &cancellables)

        viewModel.$adItems
            .receive(on: DispatchQueue.main)
            .sink { [weak self] items in
                self?.applySnapshot(items)
                self?.stopAutoScroll()
                self?.startAutoScroll()
            }
            .store(in: &cancellables)

        viewModel.$errorMessage
            .compactMap { $0 }
            .receive(on: DispatchQueue.main)
            .sink { [weak self] msg in self?.presentAlert("Home", msg) }
            .store(in: &cancellables)
    }

    private func applySnapshot(_ items: [AdViewData]) {
        var snap = NSDiffableDataSourceSnapshot<Int, AdViewData>()
        snap.appendSections([0])
        snap.appendItems(items, toSection: 0)
        dataSource.apply(snap, animatingDifferences: true)
    }

    private func startAutoScroll() {
        guard autoScrollTimer == nil else { return }
        autoScrollTimer = .scheduledTimer(withTimeInterval: autoScrollInterval, repeats: true) { [weak self] _ in
            guard let self else { return }
            let count = self.collection.numberOfItems(inSection: 0)
            guard count > 1 else { return }
            let visible = self.collection.indexPathsForVisibleItems.sorted()
            let next = (visible.last?.item ?? -1) + 1
            let target = IndexPath(item: next % count, section: 0)
            self.collection.scrollToItem(at: target, at: .centeredHorizontally, animated: true)
        }
    }

    private func stopAutoScroll() {
        autoScrollTimer?.invalidate()
        autoScrollTimer = nil
    }

    private func presentAlert(_ title: String, _ msg: String) {
        let ac = UIAlertController(title: title, message: msg, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: "OK", style: .default))
        present(ac, animated: true)
    }
}

extension HomeViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView,
                        layout collectionViewLayout: UICollectionViewLayout,
                        sizeForItemAt indexPath: IndexPath) -> CGSize {
        CGSize(width: collectionView.bounds.width, height: collectionView.bounds.height)
    }
}
