package gestao.pessoal.apresentacao.backend.engajamento.social;
import java.util.UUID;

public class SocialForm {

    private UUID amigoId;

    // Construtor vazio (obrigatório para o Spring converter o JSON)
    public SocialForm() {}

    public SocialForm(UUID amigoId) {
        this.amigoId = amigoId;
    }

    public UUID getAmigoId() {
        return amigoId;
    }

    public void setAmigoId(UUID amigoId) {
        this.amigoId = amigoId;
    }
}