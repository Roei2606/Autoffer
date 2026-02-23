import UIKit
import ProjectsSDK

final class GlassChoiceCell: UICollectionViewCell {
    
    private let imageView = UIImageView()
    private let typeLabel = UILabel()
    
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
        
        typeLabel.font = .systemFont(ofSize: 14, weight: .medium)
        typeLabel.textAlignment = .center
        typeLabel.textColor = .darkGray
        typeLabel.numberOfLines = 2
        
        let stack = UIStackView(arrangedSubviews: [imageView, typeLabel])
        stack.axis = .vertical
        stack.spacing = 4
        stack.translatesAutoresizingMaskIntoConstraints = false
        
        contentView.addSubview(stack)
        
        NSLayoutConstraint.activate([
            imageView.heightAnchor.constraint(equalToConstant: 80),
            stack.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 8),
            stack.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 8),
            stack.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -8),
            stack.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -8)
        ])
    }
    
    func configure(with glass: Glass) {
        if let image = glass.uiImage {
            imageView.image = image
        } else {
            imageView.image = UIImage(systemName: "rectangle.fill")
        }
        typeLabel.text = glass.type ?? "Unknown"
    }

    
    override func prepareForReuse() {
        super.prepareForReuse()
        imageView.image = UIImage(systemName: "rectangle.fill")
        typeLabel.text = nil
    }
}
