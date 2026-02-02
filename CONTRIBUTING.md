# Contributing to cGenerators

Thank you for considering contributing to cGenerators! 🎉

## Quick Start

1. Fork the repository
2. Create a branch (`git checkout -b feature/AmazingFeature`)
3. Make your changes
4. Test thoroughly
5. Commit (`git commit -m 'feat: add AmazingFeature'`)
6. Push (`git push origin feature/AmazingFeature`)
7. Open a Pull Request

## Development Setup

```bash
git clone https://github.com/yourusername/cGenerators.git
cd cGenerators
mvn clean package
```

## Code Style

- Java: PascalCase for classes, camelCase for methods/variables
- YAML: lowercase-with-hyphens
- Commit messages: Use [Conventional Commits](https://www.conventionalcommits.org/)

## Adding Languages

1. Copy `locale/en.yml` to `locale/[code].yml`
2. Translate all strings
3. Test in-game
4. Update README

Questions? Open an issue or join our Discord!
