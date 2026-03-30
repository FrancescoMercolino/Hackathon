--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

-- Started on 2026-03-30 23:07:28

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 906 (class 1247 OID 33361)
-- Name: stato_hackathon; Type: DOMAIN; Schema: public; Owner: -
--

CREATE DOMAIN public.stato_hackathon AS character varying(20)
	CONSTRAINT stato_hackathon_check CHECK (((VALUE)::text = ANY ((ARRAY['aperto'::character varying, 'chiuse'::character varying, 'terminate'::character varying])::text[])));


--
-- TOC entry 249 (class 1255 OID 33384)
-- Name: aggiorna_esaminazione_f(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.aggiorna_esaminazione_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	INSERT INTO esamina VALUES (NEW.nome_giudice, NEW.nome_team);
	RETURN NEW;
END;
$$;


--
-- TOC entry 232 (class 1255 OID 24948)
-- Name: aggiorna_iscrizione_hackathon_f(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.aggiorna_iscrizione_hackathon_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
UPDATE team SET hackathon = new.hackathon WHERE nome_team = NEW.nome_team;
RETURN NEW;
END;
$$;


--
-- TOC entry 230 (class 1255 OID 33297)
-- Name: aggiungi_giudice_f(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.aggiungi_giudice_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
DELETE FROM partecipante
WHERE nome = NEW.nome;

RETURN NEW;
END;
$$;


--
-- TOC entry 235 (class 1255 OID 33373)
-- Name: appartenenza_unica_f(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.appartenenza_unica_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
IF OLD.nome_team IS NOT NULL THEN
	RAISE EXCEPTION 'Il partecipante fa già parte di un team';
	END IF;
	RETURN NEW;
	END;
	$$;


--
-- TOC entry 253 (class 1255 OID 33395)
-- Name: blocca_inserimento_team_f(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.blocca_inserimento_team_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	IF NOT EXISTS (SELECT * FROM partecipante WHERE nome_team = NEW.nome_team ) THEN
	RAISE EXCEPTION 'I team non possono essere inseriti senza partecipanti';
	END IF;
	RETURN NEW;
END;
$$;


--
-- TOC entry 255 (class 1255 OID 33398)
-- Name: conta_iscritti_hackathon_d_f(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.conta_iscritti_hackathon_d_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	UPDATE hackathon
	SET numeroiscritti = (SELECT COUNT(*) from team where hackathon = OLD.hackathon)
	WHERE titolo = OLD.hackathon;
	RETURN OLD;
END;
$$;


--
-- TOC entry 254 (class 1255 OID 33378)
-- Name: conta_iscritti_hackathon_f(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.conta_iscritti_hackathon_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	UPDATE hackathon
	SET numeroiscritti = (SELECT COUNT(*) from team where hackathon = NEW.hackathon)
	WHERE titolo = NEW.hackathon;
	RETURN NEW;
END;
$$;


--
-- TOC entry 251 (class 1255 OID 33382)
-- Name: controlla_inviti_f(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.controlla_inviti_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
  IF NEW.nome_team IS DISTINCT FROM OLD.nome_team THEN
    IF (
      SELECT h.stato
      FROM team t
      JOIN hackathon h ON h.titolo = t.hackathon
      WHERE t.nome_team = NEW.nome_team
    ) <> 'aperto'
    THEN
      RAISE EXCEPTION 'Non puoi aggiungere altri partecipanti';
    END IF;
  END IF;
  RETURN NEW;
END;
$$;


--
-- TOC entry 237 (class 1255 OID 33380)
-- Name: controlla_stato_hackathon_f(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.controlla_stato_hackathon_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
IF (SELECT stato FROM hackathon WHERE titolo = NEW.hackathon) <> 'aperto'
 THEN RAISE EXCEPTION 'Iscrizioni chiuse';
 END IF;
 RETURN NEW;
 END;
 $$;


--
-- TOC entry 250 (class 1255 OID 33390)
-- Name: controllo_username_unico_f(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.controllo_username_unico_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM partecipante WHERE nome = NEW.nome) OR
       EXISTS (SELECT 1 FROM giudice WHERE nome = NEW.nome) OR
       EXISTS (SELECT 1 FROM organizzatore WHERE nome = NEW.nome) THEN
       RAISE EXCEPTION 'Username % già esistente', NEW.nome;
    END IF;
    RETURN NEW;
END;
$$;


--
-- TOC entry 252 (class 1255 OID 33307)
-- Name: crea_team_f(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.crea_team_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
	IF NEW.nome_team IS DISTINCT FROM OLD.nome_team THEN
	INSERT INTO team (nome_team)
	VALUES (NEW.nome_team)
	ON CONFLICT (nome_team) DO NOTHING;
	END IF;
RETURN NEW;
END;
$$;


--
-- TOC entry 236 (class 1255 OID 33375)
-- Name: iscrizione_unica_f(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.iscrizione_unica_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
IF OLD.hackathon IS NOT NULL THEN RAISE EXCEPTION 'Siete già iscritti ad un altro hackathon';
END IF;
RETURN NEW;
END;
$$;


--
-- TOC entry 233 (class 1255 OID 33349)
-- Name: pubblica_problema(text, text, text); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.pubblica_problema(p_descrizione text, p_hackathon text, p_giudice text) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    INSERT INTO problema(descrizione_problema, hackathon)
    VALUES (p_descrizione, p_hackathon);

    INSERT INTO pubblica(descrizione, nome_giudice, hackathon)
    VALUES (p_descrizione, p_giudice, p_hackathon);
END;
$$;


--
-- TOC entry 234 (class 1255 OID 33358)
-- Name: sovrascrivi_problema_f(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.sovrascrivi_problema_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
DELETE FROM problema WHERE hackathon = NEW.hackathon;
RETURN NEW;
END;
$$;


--
-- TOC entry 231 (class 1255 OID 24944)
-- Name: sovrascrivi_soluzione_f(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.sovrascrivi_soluzione_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
DELETE FROM documento WHERE team_nome = NEW.team_nome;
RETURN NEW;
END;
$$;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 219 (class 1259 OID 16601)
-- Name: team; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.team (
    nome_team character varying(20) NOT NULL,
    hackathon character varying(50)
);


--
-- TOC entry 226 (class 1259 OID 33278)
-- Name: voto; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.voto (
    nome_giudice character varying(20) NOT NULL,
    nome_team character varying(20) NOT NULL,
    valore integer NOT NULL,
    CONSTRAINT voto_tra_1_10 CHECK (((valore > 0) AND (valore < 11)))
);


--
-- TOC entry 229 (class 1259 OID 33399)
-- Name: classifica; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW public.classifica AS
 SELECT t.hackathon,
    v.nome_team,
    sum(v.valore) AS punti
   FROM (public.voto v
     JOIN public.team t ON (((v.nome_team)::text = (t.nome_team)::text)))
  GROUP BY t.hackathon, v.nome_team
  ORDER BY (sum(v.valore)) DESC;


--
-- TOC entry 221 (class 1259 OID 16743)
-- Name: documento; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.documento (
    descrizione text,
    team_nome character varying(20) NOT NULL
);


--
-- TOC entry 225 (class 1259 OID 33236)
-- Name: esamina; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.esamina (
    nome_giudice character varying(20) NOT NULL,
    nome_team character varying(20) NOT NULL
);


--
-- TOC entry 223 (class 1259 OID 33154)
-- Name: giudice; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.giudice (
    nome character varying(20) NOT NULL,
    pwd character varying(20) NOT NULL,
    nome_organizzatore character varying(20) NOT NULL
);


--
-- TOC entry 217 (class 1259 OID 16484)
-- Name: hackathon; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.hackathon (
    titolo character varying(50) NOT NULL,
    sede character varying(20) NOT NULL,
    datainizio date,
    datfine date,
    numeroiscritti integer,
    stato public.stato_hackathon DEFAULT 'chiuse'::character varying,
    numeromaxteam integer
);


--
-- TOC entry 222 (class 1259 OID 33134)
-- Name: organizzatore; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.organizzatore (
    nome character varying(20) NOT NULL,
    pwd character varying(20) NOT NULL
);


--
-- TOC entry 218 (class 1259 OID 16487)
-- Name: partecipante; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.partecipante (
    nome character varying(20) NOT NULL,
    pwd character varying(20) NOT NULL,
    nome_team character varying(20),
    CONSTRAINT lunghezza_pwd CHECK ((length((pwd)::text) > 5))
);


--
-- TOC entry 227 (class 1259 OID 33299)
-- Name: login; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW public.login AS
 SELECT partecipante.nome,
    partecipante.pwd
   FROM public.partecipante
UNION ALL
 SELECT giudice.nome,
    giudice.pwd
   FROM public.giudice
UNION ALL
 SELECT organizzatore.nome,
    organizzatore.pwd
   FROM public.organizzatore;


--
-- TOC entry 220 (class 1259 OID 16731)
-- Name: problema; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.problema (
    descrizione_problema text NOT NULL,
    hackathon character varying(50) NOT NULL
);


--
-- TOC entry 224 (class 1259 OID 33214)
-- Name: pubblica; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pubblica (
    descrizione text NOT NULL,
    nome_giudice character varying(20) NOT NULL,
    hackathon character varying(50) NOT NULL
);


--
-- TOC entry 228 (class 1259 OID 33386)
-- Name: tipo_utente; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW public.tipo_utente AS
 SELECT 'partecipante'::text AS tipo,
    partecipante.nome,
    partecipante.pwd
   FROM public.partecipante
UNION ALL
 SELECT 'giudice'::text AS tipo,
    giudice.nome,
    giudice.pwd
   FROM public.giudice
UNION ALL
 SELECT 'organizzatore'::text AS tipo,
    organizzatore.nome,
    organizzatore.pwd
   FROM public.organizzatore;


--
-- TOC entry 4906 (class 0 OID 16743)
-- Dependencies: 221
-- Data for Name: documento; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.documento (descrizione, team_nome) FROM stdin;
Ho i capelli belli	Capelloni
Mi voglio ubriacare	Barboni
	TastiniMatti
\.


--
-- TOC entry 4910 (class 0 OID 33236)
-- Dependencies: 225
-- Data for Name: esamina; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.esamina (nome_giudice, nome_team) FROM stdin;
Raffio	DancingVirus
Raffio	Capelloni
Raffio	Barboni
Raffio	RubberBand
Raffio	habemus
Raffio	ciao
Raffio	spiaggiati
Raffio	TastiniMatti
\.


--
-- TOC entry 4908 (class 0 OID 33154)
-- Dependencies: 223
-- Data for Name: giudice; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.giudice (nome, pwd, nome_organizzatore) FROM stdin;
Raffio	Bottom	Francesco
Angiulella	99Nights	Francesco
\.


--
-- TOC entry 4902 (class 0 OID 16484)
-- Dependencies: 217
-- Data for Name: hackathon; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.hackathon (titolo, sede, datainizio, datfine, numeroiscritti, stato, numeromaxteam) FROM stdin;
Bits and click	milano	2026-03-05	2026-03-08	100	terminate	50
virus rain	milano	2025-08-10	2025-08-15	50	terminate	20
Virus CUP 26	Napoli	2026-09-15	2026-09-18	50	aperto	25
ALU CUP	Milano	2026-12-09	2026-12-12	50	chiuse	25
\.


--
-- TOC entry 4907 (class 0 OID 33134)
-- Dependencies: 222
-- Data for Name: organizzatore; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.organizzatore (nome, pwd) FROM stdin;
Francesco	pessi
\.


--
-- TOC entry 4903 (class 0 OID 16487)
-- Dependencies: 218
-- Data for Name: partecipante; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.partecipante (nome, pwd, nome_team) FROM stdin;
PeppeFerro	forma2025	DancingVirus
Giuli45	piccolo	DancingVirus
LucaBazzi	biondo3000	Capelloni
prova	proviamo	Barboni
manichino	manichino	DancingVirus
ilmaestro	zonadestra	DancingVirus
Gear5	luffytaro	RubberBand
Maurizio	ilvizio	habemus
Cuffie	pantalone	ciao
tablet	tablet	spiaggiati
ciaone	ciaone	habemus
panino	panino	burger
carne	carne11	burger
biscotti	biscotti	halleluja
lorenzo	lorenzo	halleluja
mariano	mariano	dottor virus
RYKOMAS	tftgod	casa
tastiera	tastiera	TastiniMatti
CAPSLOCK	capslock	TastiniMatti
\.


--
-- TOC entry 4905 (class 0 OID 16731)
-- Dependencies: 220
-- Data for Name: problema; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.problema (descrizione_problema, hackathon) FROM stdin;
Scrivere due funzioni in C	virus rain
	Bits and click
Spiegare al giudice come si fa ad andare a comprare le sigarette	ALU CUP
\.


--
-- TOC entry 4909 (class 0 OID 33214)
-- Dependencies: 224
-- Data for Name: pubblica; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.pubblica (descrizione, nome_giudice, hackathon) FROM stdin;
Scrivere due funzioni in C	Raffio	virus rain
	Raffio	Bits and click
Spiegare al giudice come si fa ad andare a comprare le sigarette	Raffio	ALU CUP
\.


--
-- TOC entry 4904 (class 0 OID 16601)
-- Dependencies: 219
-- Data for Name: team; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.team (nome_team, hackathon) FROM stdin;
DancingVirus	virus rain
Capelloni	virus rain
Barboni	virus rain
RubberBand	virus rain
ciao	Bits and click
spiaggiati	Bits and click
habemus	virus rain
burger	Virus CUP 26
halleluja	Virus CUP 26
dottor virus	Virus CUP 26
casa	\N
TastiniMatti	ALU CUP
\.


--
-- TOC entry 4911 (class 0 OID 33278)
-- Dependencies: 226
-- Data for Name: voto; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.voto (nome_giudice, nome_team, valore) FROM stdin;
Raffio	DancingVirus	10
Raffio	Capelloni	8
Raffio	Barboni	7
Raffio	RubberBand	6
Raffio	habemus	5
Raffio	ciao	10
Raffio	spiaggiati	10
Raffio	TastiniMatti	5
\.


--
-- TOC entry 4719 (class 2606 OID 24943)
-- Name: documento documento_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.documento
    ADD CONSTRAINT documento_pk PRIMARY KEY (team_nome);


--
-- TOC entry 4727 (class 2606 OID 33250)
-- Name: esamina esamina_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.esamina
    ADD CONSTRAINT esamina_pk PRIMARY KEY (nome_giudice, nome_team);


--
-- TOC entry 4723 (class 2606 OID 33182)
-- Name: giudice giudice_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.giudice
    ADD CONSTRAINT giudice_pk PRIMARY KEY (nome);


--
-- TOC entry 4711 (class 2606 OID 16558)
-- Name: hackathon hackathon_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hackathon
    ADD CONSTRAINT hackathon_pk PRIMARY KEY (titolo);


--
-- TOC entry 4721 (class 2606 OID 33153)
-- Name: organizzatore organizzatore_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.organizzatore
    ADD CONSTRAINT organizzatore_pk PRIMARY KEY (nome);


--
-- TOC entry 4717 (class 2606 OID 33324)
-- Name: problema problema_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.problema
    ADD CONSTRAINT problema_pk PRIMARY KEY (hackathon);


--
-- TOC entry 4725 (class 2606 OID 33342)
-- Name: pubblica pubblica_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pubblica
    ADD CONSTRAINT pubblica_pk PRIMARY KEY (hackathon, nome_giudice);


--
-- TOC entry 4715 (class 2606 OID 16608)
-- Name: team team_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.team
    ADD CONSTRAINT team_pkey PRIMARY KEY (nome_team);


--
-- TOC entry 4713 (class 2606 OID 16659)
-- Name: partecipante utente_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.partecipante
    ADD CONSTRAINT utente_pkey PRIMARY KEY (nome);


--
-- TOC entry 4729 (class 2606 OID 33327)
-- Name: voto voto_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.voto
    ADD CONSTRAINT voto_pk PRIMARY KEY (nome_team, nome_giudice);


--
-- TOC entry 4753 (class 2620 OID 33385)
-- Name: voto aggiorna_esaminazione; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER aggiorna_esaminazione AFTER INSERT ON public.voto FOR EACH ROW EXECUTE FUNCTION public.aggiorna_esaminazione_f();


--
-- TOC entry 4752 (class 2620 OID 33298)
-- Name: giudice aggiungi_giudice; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER aggiungi_giudice AFTER INSERT ON public.giudice FOR EACH ROW EXECUTE FUNCTION public.aggiungi_giudice_f();


--
-- TOC entry 4740 (class 2620 OID 33374)
-- Name: partecipante appartenenza_unica; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER appartenenza_unica BEFORE UPDATE ON public.partecipante FOR EACH ROW EXECUTE FUNCTION public.appartenenza_unica_f();


--
-- TOC entry 4744 (class 2620 OID 33396)
-- Name: team blocca_inserimento_team; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER blocca_inserimento_team BEFORE INSERT ON public.team FOR EACH ROW EXECUTE FUNCTION public.blocca_inserimento_team_f();


--
-- TOC entry 4745 (class 2620 OID 33379)
-- Name: team conta_iscritti_hackathon; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER conta_iscritti_hackathon AFTER INSERT ON public.team FOR EACH ROW EXECUTE FUNCTION public.conta_iscritti_hackathon_f();


--
-- TOC entry 4746 (class 2620 OID 33397)
-- Name: team conta_iscritti_hackathon_d; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER conta_iscritti_hackathon_d AFTER DELETE ON public.team FOR EACH ROW EXECUTE FUNCTION public.conta_iscritti_hackathon_d_f();


--
-- TOC entry 4741 (class 2620 OID 33383)
-- Name: partecipante controlla_inviti; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER controlla_inviti BEFORE UPDATE ON public.partecipante FOR EACH ROW EXECUTE FUNCTION public.controlla_inviti_f();


--
-- TOC entry 4747 (class 2620 OID 33381)
-- Name: team controlla_stato_hackathon; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER controlla_stato_hackathon BEFORE INSERT OR UPDATE ON public.team FOR EACH ROW EXECUTE FUNCTION public.controlla_stato_hackathon_f();


--
-- TOC entry 4742 (class 2620 OID 33308)
-- Name: partecipante crea_team; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER crea_team AFTER UPDATE ON public.partecipante FOR EACH ROW EXECUTE FUNCTION public.crea_team_f();


--
-- TOC entry 4748 (class 2620 OID 33376)
-- Name: team iscrizione_unica; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER iscrizione_unica BEFORE UPDATE ON public.team FOR EACH ROW EXECUTE FUNCTION public.iscrizione_unica_f();


--
-- TOC entry 4751 (class 2620 OID 33393)
-- Name: organizzatore organizzatore_username_unico; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER organizzatore_username_unico BEFORE INSERT ON public.organizzatore FOR EACH ROW EXECUTE FUNCTION public.controllo_username_unico_f();


--
-- TOC entry 4743 (class 2620 OID 33391)
-- Name: partecipante partecipante_username_unico; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER partecipante_username_unico BEFORE INSERT ON public.partecipante FOR EACH ROW EXECUTE FUNCTION public.controllo_username_unico_f();


--
-- TOC entry 4749 (class 2620 OID 33359)
-- Name: problema sovrascrivi_problema; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER sovrascrivi_problema BEFORE INSERT ON public.problema FOR EACH ROW EXECUTE FUNCTION public.sovrascrivi_problema_f();


--
-- TOC entry 4750 (class 2620 OID 24945)
-- Name: documento sovrascrivi_soluzione; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER sovrascrivi_soluzione BEFORE INSERT ON public.documento FOR EACH ROW EXECUTE FUNCTION public.sovrascrivi_soluzione_f();


--
-- TOC entry 4736 (class 2606 OID 33239)
-- Name: esamina esamina_giudice_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.esamina
    ADD CONSTRAINT esamina_giudice_fk FOREIGN KEY (nome_giudice) REFERENCES public.giudice(nome);


--
-- TOC entry 4737 (class 2606 OID 33244)
-- Name: esamina esamina_team; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.esamina
    ADD CONSTRAINT esamina_team FOREIGN KEY (nome_team) REFERENCES public.team(nome_team);


--
-- TOC entry 4733 (class 2606 OID 33313)
-- Name: giudice giudice_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.giudice
    ADD CONSTRAINT giudice_fk FOREIGN KEY (nome_organizzatore) REFERENCES public.organizzatore(nome);


--
-- TOC entry 4730 (class 2606 OID 16609)
-- Name: team hackathon_team; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.team
    ADD CONSTRAINT hackathon_team FOREIGN KEY (hackathon) REFERENCES public.hackathon(titolo);


--
-- TOC entry 4731 (class 2606 OID 33318)
-- Name: problema problema_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.problema
    ADD CONSTRAINT problema_fk FOREIGN KEY (hackathon) REFERENCES public.hackathon(titolo);


--
-- TOC entry 4734 (class 2606 OID 33221)
-- Name: pubblica pubblica_giudice_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pubblica
    ADD CONSTRAINT pubblica_giudice_fk FOREIGN KEY (nome_giudice) REFERENCES public.giudice(nome);


--
-- TOC entry 4735 (class 2606 OID 33350)
-- Name: pubblica pubblica_problema_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pubblica
    ADD CONSTRAINT pubblica_problema_fk FOREIGN KEY (hackathon) REFERENCES public.problema(hackathon) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 4732 (class 2606 OID 16785)
-- Name: documento team_esiste; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.documento
    ADD CONSTRAINT team_esiste FOREIGN KEY (team_nome) REFERENCES public.team(nome_team);


--
-- TOC entry 4738 (class 2606 OID 33283)
-- Name: voto voto_giudice_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.voto
    ADD CONSTRAINT voto_giudice_fk FOREIGN KEY (nome_giudice) REFERENCES public.giudice(nome);


--
-- TOC entry 4739 (class 2606 OID 33288)
-- Name: voto voto_team_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.voto
    ADD CONSTRAINT voto_team_fk FOREIGN KEY (nome_team) REFERENCES public.team(nome_team);


-- Completed on 2026-03-30 23:07:29

--
-- PostgreSQL database dump complete
--

