package br.com.boleto;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.Aceite;

public class PrimeiroBoleto {
	
	public static void main(String[] args) {
		// Cedente: quem envia o boleto
		Cedente cedente = new Cedente("IGEPP - Instituto de Gestão, Economia e Políticas Públicas",
				"10.687.566/0001-97");
		
		// Sacado: quem recebe o boleto (pagante)
		Sacado sacado = new Sacado("Lidiane dos Santos ALves");
		
		// Endereço: do Sacado
		Endereco endereco = new Endereco();
		endereco.setUF(UnidadeFederativa.DF);
		endereco.setLocalidade("Brasília");
		endereco.setCep(new CEP("10100-000"));
		endereco.setBairro("W3 Sul");
		endereco.setLogradouro("L2");
		endereco.setNumero("1010");
		
		// Adiciona: Endereço para o Sacado
		sacado.addEndereco(endereco);
		
		// ContaBancaria: dados no Banco que vai receber o boleto
		ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.BANCO_DO_BRASIL.create());
		contaBancaria.setAgencia(new Agencia(001, "9"));
		contaBancaria.setNumeroDaConta(new NumeroDaConta(2020, "0"));
		contaBancaria.setCarteira(new Carteira(6));
		
		// Título: recebe todos os dados acima (conta, sacado, cedente)
		Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
		titulo.setNumeroDoDocumento("101010");
		titulo.setNossoNumero("12345678901");
		titulo.setDigitoDoNossoNumero("P");
		
		titulo.setValor(BigDecimal.valueOf(100.23));
		titulo.setDataDoDocumento(new Date());
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(2016, 9, 20); // 20 de Outubro de 2016. *Obs.: Janeiro começa com '0'
		
		titulo.setDataDoVencimento(calendar.getTime());
		
		titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);
		
		titulo.setAceite(Aceite.N);
		
		// Boleto: dados do boleto (Gera boleto)
		Boleto boleto = new Boleto(titulo);
		boleto.setLocalPagamento("Pagar preferêncialmente no Bradesco.");
		boleto.setInstrucaoAoSacado("Evite multas, pague em dia seu boleto.");
		
		boleto.setInstrucao1("Após o vencimento, aplicar multa de 2,00% e juros de 1,00% ao mês.");
		
		BoletoViewer boletoViewer = new BoletoViewer(boleto);
		File arquivoPdf = boletoViewer.getPdfAsFile("meu-primeiro-boleto.pdf");
		
		mostrarNaTela(arquivoPdf);
		
	}
	
	// Mostrar boleto bancario (arquivo pdf) na tela
	private static void mostrarNaTela(File arquivo) {
		Desktop desktop = Desktop.getDesktop();
		
		try {
			desktop.open(arquivo);
		} catch (IOException e) {
			e.getMessage();
			e.printStackTrace();
		}
	}

}
