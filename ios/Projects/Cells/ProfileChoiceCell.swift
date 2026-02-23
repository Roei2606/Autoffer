import UIKit
import ProjectsSDK

final class ProfileChoiceCell: UICollectionViewCell {
    
    private let imageView = UIImageView()
    private let numberLabel = UILabel()
    private let typeLabel = UILabel()
    private let priceLabel = UILabel()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupUI() {
        contentView.backgroundColor = .white
        contentView.layer.cornerRadius = 12
        contentView.layer.shadowColor = UIColor.black.cgColor
        contentView.layer.shadowOpacity = 0.1
        contentView.layer.shadowRadius = 4
        contentView.layer.shadowOffset = CGSize(width: 0, height: 2)
        
        imageView.contentMode = .scaleAspectFill
        imageView.layer.cornerRadius = 8
        imageView.clipsToBounds = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        
        numberLabel.font = .boldSystemFont(ofSize: 16)
        numberLabel.textAlignment = .center
        
        typeLabel.font = .systemFont(ofSize: 14)
        typeLabel.textColor = .darkGray
        typeLabel.textAlignment = .center
        
        priceLabel.font = .systemFont(ofSize: 14)
        priceLabel.textColor = .systemGray
        priceLabel.textAlignment = .center
        
        let stack = UIStackView(arrangedSubviews: [imageView, numberLabel, typeLabel, priceLabel])
        stack.axis = .vertical
        stack.spacing = 4
        stack.translatesAutoresizingMaskIntoConstraints = false
        
        contentView.addSubview(stack)
        
        NSLayoutConstraint.activate([
            imageView.heightAnchor.constraint(equalToConstant: 100),
            stack.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 8),
            stack.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 8),
            stack.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -8),
            stack.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -8)
        ])
    }
    
    func configure(with profile: AlumProfile) {
        if let image = profile.uiImage {
            imageView.image = image
        } else {
            imageView.image = UIImage(systemName: "photo")
        }
        
        numberLabel.text = profile.profileNumber ?? "Unknown"
        typeLabel.text = profile.usageType?.uppercased() ?? "N/A"
        priceLabel.text = profile.isExpensive == true ? "$$" : "$"
    }

    
    override func prepareForReuse() {
        super.prepareForReuse()
        imageView.image = UIImage(systemName: "photo")
        numberLabel.text = nil
        typeLabel.text = nil
        priceLabel.text = nil
    }
}
