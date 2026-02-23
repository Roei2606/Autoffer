import UIKit
import ProjectsSDK   // AlumProfile, Glass
import CoreModelsSDK // AlumProfileDTO, GlassDTO
import LocalProjectSDK

final class AddManualViewController: UIViewController {
    
    private let viewModel = AddManualViewModel()
    
    private let widthField = UITextField()
    private let heightField = UITextField()
    private let findButton = UIButton(type: .system)
    private let profilesCollection = UICollectionView(frame: .zero,
                                                      collectionViewLayout: UICollectionViewFlowLayout())
    private let glassesCollection = UICollectionView(frame: .zero,
                                                     collectionViewLayout: UICollectionViewFlowLayout())
    private let positionField = UITextField()
    private let quantityField = UITextField()
    private let addButton = UIButton(type: .system)
    
    override func viewDidLoad() {
        super.viewDidLoad()
        title = "Add Product Manually"
        view.backgroundColor = .systemBackground
        setupUI()
    }
    
    private func setupUI() {
        // widthField
        widthField.placeholder = "Width (cm)"
        widthField.borderStyle = .roundedRect
        widthField.keyboardType = .numberPad
        
        // heightField
        heightField.placeholder = "Height (cm)"
        heightField.borderStyle = .roundedRect
        heightField.keyboardType = .numberPad
        
        // findButton
        findButton.setTitle("Find Matching Profiles", for: .normal)
        findButton.setTitleColor(.white, for: .normal)
        findButton.backgroundColor = .black
        findButton.layer.cornerRadius = 8
        findButton.addTarget(self, action: #selector(onFindProfiles), for: .touchUpInside)
        
        // profilesCollection
        if let layout = profilesCollection.collectionViewLayout as? UICollectionViewFlowLayout {
            layout.scrollDirection = .horizontal
            layout.minimumLineSpacing = 12
        }
        profilesCollection.backgroundColor = .clear
        profilesCollection.register(ProfileChoiceCell.self, forCellWithReuseIdentifier: "ProfileCell")
        profilesCollection.delegate = self
        profilesCollection.dataSource = self
        profilesCollection.isHidden = true
        
        // glassesCollection
        if let layout = glassesCollection.collectionViewLayout as? UICollectionViewFlowLayout {
            layout.scrollDirection = .horizontal
            layout.minimumLineSpacing = 12
        }
        glassesCollection.backgroundColor = .clear
        glassesCollection.register(GlassChoiceCell.self, forCellWithReuseIdentifier: "GlassCell")
        glassesCollection.delegate = self
        glassesCollection.dataSource = self
        glassesCollection.isHidden = true
        
        // positionField
        positionField.placeholder = "Position in Property"
        positionField.borderStyle = .roundedRect
        positionField.isHidden = true
        
        // quantityField
        quantityField.placeholder = "Quantity"
        quantityField.borderStyle = .roundedRect
        quantityField.keyboardType = .numberPad
        quantityField.isHidden = true
        
        // addButton
        addButton.setTitle("Add To Project", for: .normal)
        addButton.setTitleColor(.white, for: .normal)
        addButton.backgroundColor = .black
        addButton.layer.cornerRadius = 8
        addButton.isHidden = true
        addButton.addTarget(self, action: #selector(onAddItem), for: .touchUpInside)
        
        // Layout
        let stack = UIStackView(arrangedSubviews: [
            widthField, heightField, findButton,
            profilesCollection, glassesCollection,
            positionField, quantityField, addButton
        ])
        stack.axis = .vertical
        stack.spacing = 16
        stack.translatesAutoresizingMaskIntoConstraints = false
        
        view.addSubview(stack)
        
        NSLayoutConstraint.activate([
            stack.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            stack.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            stack.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            stack.bottomAnchor.constraint(lessThanOrEqualTo: view.safeAreaLayoutGuide.bottomAnchor)
        ])
        
        profilesCollection.heightAnchor.constraint(equalToConstant: 160).isActive = true
        glassesCollection.heightAnchor.constraint(equalToConstant: 140).isActive = true
    }

    
    @objc private func onFindProfiles() {
        guard let wText = widthField.text, let width = Int(wText),
              let hText = heightField.text, let height = Int(hText) else { return }
        
        Task {
            await viewModel.findProfiles(width: width, height: height)
            profilesCollection.isHidden = false
            glassesCollection.isHidden = true
            positionField.isHidden = true
            quantityField.isHidden = true
            addButton.isHidden = true
            profilesCollection.reloadData()
        }
    }
    
    @objc private func onAddItem() {
        Task {
            guard let pos = positionField.text, !pos.isEmpty,
                  let qText = quantityField.text, let qty = Int(qText),
                  let hText = heightField.text, let height = Double(hText),
                  let wText = widthField.text, let width = Double(wText) else { return }
            
            await viewModel.addItemToProject(width: width, height: height, quantity: qty, position: pos)
            
            await MainActor.run {
                let alert = UIAlertController(title: "Item Added",
                                              message: "Do you want to add another product?",
                                              preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "Yes", style: .default) { _ in self.resetForm() })
                alert.addAction(UIAlertAction(title: "No", style: .cancel) { _ in
                    self.navigationController?.popViewController(animated: true)
                })
                present(alert, animated: true)
            }
        }
    }
    
    private func resetForm() {
        widthField.text = ""
        heightField.text = ""
        positionField.text = ""
        quantityField.text = ""
        profilesCollection.isHidden = true
        glassesCollection.isHidden = true
        positionField.isHidden = true
        quantityField.isHidden = true
        addButton.isHidden = true
    }
}

extension AddManualViewController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return collectionView == profilesCollection ? viewModel.profiles.count : viewModel.glasses.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if collectionView == profilesCollection {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "ProfileCell", for: indexPath) as! ProfileChoiceCell
            cell.configure(with: viewModel.profiles[indexPath.item])
            return cell
        } else {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "GlassCell", for: indexPath) as! GlassChoiceCell
            cell.configure(with: viewModel.glasses[indexPath.item])
            return cell
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if collectionView == profilesCollection {
            let profile = viewModel.profiles[indexPath.item]
            Task {
                await viewModel.loadGlasses(for: profile)
                glassesCollection.isHidden = false
                glassesCollection.reloadData()
            }
        } else {
            viewModel.selectedGlass = viewModel.glasses[indexPath.item]
            positionField.isHidden = false
            quantityField.isHidden = false
            addButton.isHidden = false
        }
    }
    
    func collectionView(_ collectionView: UICollectionView,
                        layout collectionViewLayout: UICollectionViewLayout,
                        sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: 140, height: 160)
    }
}
